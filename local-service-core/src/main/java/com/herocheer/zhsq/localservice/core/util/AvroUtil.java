package com.herocheer.zhsq.localservice.core.util;

import okio.Buffer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for Avro binary data and object conversion
 */
public class AvroUtil {
    /**
     * Binary decoding into Avro objects
     *
     * @param schema
     * @param recordBytes
     * @return List<GenericRecord>
     * @throws IOException
     */
    public static List<GenericRecord> decode(Schema schema, byte[] recordBytes) throws IOException {
        DatumReader<GenericRecord> datumReader = new SpecificDatumReader<>(schema);
        ByteArrayInputStream stream = new ByteArrayInputStream(recordBytes);
        stream.reset();
        BinaryDecoder binaryDecoder = new DecoderFactory().binaryDecoder(stream, null);
        List<GenericRecord> records = new ArrayList<>();
        while (true) {
            try {
                GenericRecord record = datumReader.read(null, binaryDecoder);
                records.add(record);
            } catch (EOFException e) {
                break;
            }
        }
        return records;
    }

    /**
     * Binary decoding into Avro objects
     *
     * @param writeSchemaStr writeSchema
     * @param readSchema     readSchema
     * @param recordBytes
     * @return List<GenericRecord>
     * @throws IOException
     */
    public static List<GenericRecord> decodeCompatibility(String writeSchemaStr, Schema readSchema, byte[] recordBytes) throws IOException {
        if (writeSchemaStr == null) {
            return decode(readSchema, recordBytes);
        } else {
            Schema writeSchema = Schema.parse(writeSchemaStr);
            DatumReader<GenericRecord> datumReader = new SpecificDatumReader<GenericRecord>(writeSchema, readSchema);

            BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(recordBytes, null);
            List<GenericRecord> records = new ArrayList<GenericRecord>();
            while (true) {
                try {
                    GenericRecord record = datumReader.read(null, decoder);
                    records.add(record);
                } catch (EOFException e) {
                    break;
                }
            }
            return records;
        }
    }

    /**
     * Avro object into binary for HTTP transmission
     *
     * @param schema
     * @param records
     * @return Buffer
     * @throws IOException
     */
    public static Buffer encode(Schema schema, List<GenericRecord> records) throws IOException {
        Buffer buffer = new Buffer();
        SpecificDatumWriter<GenericRecord> datumWriter = new SpecificDatumWriter<>(schema);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.reset();
        BinaryEncoder binaryEncoder = new EncoderFactory().binaryEncoder(byteArrayOutputStream, null);
        for (GenericRecord segment : records) {
            datumWriter.write(segment, binaryEncoder);
        }
        binaryEncoder.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();
        buffer.write(bytes);
        return buffer;
    }

    public static byte[] encode(Schema schema, GenericRecord record) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            SpecificDatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
            BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(baos, null);
            writer.write(record, binaryEncoder);
            binaryEncoder.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }
//
//    public static byte[] encodeRepoRequest(EditRepositoryRequest editRepositoryRequest) {
//        try (OutputStream bytesOutpustStream = new ByteArrayOutputStream()) {
//            DatumWriter<EditRepositoryRequest> writer = new SpecificDatumWriter<>(EditRepositoryRequest.class);
//            Encoder encoder = EncoderFactory.get().binaryEncoder(bytesOutpustStream, null);
//            writer.write(editRepositoryRequest, encoder);
//            encoder.flush();
//            return ((ByteArrayOutputStream) bytesOutpustStream).toByteArray();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
