package com.costello.dataflow;

import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FileToBytesDoFn extends DoFn<FileIO.ReadableFile, KV<String, byte[]>> {

    private static final Logger LOG = LoggerFactory.getLogger(FileToBytesDoFn.class);

    @ProcessElement
    public void process(@Element FileIO.ReadableFile readableFile, OutputReceiver<KV<String, byte[]>> out) {

        try {
            String resourcePath = readableFile.getMetadata().resourceId().toString();
            byte[] bytes = readableFile.readFullyAsBytes();

            out.output(KV.of(resourcePath, bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}