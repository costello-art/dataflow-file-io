package com.costello.dataflow;

import com.costello.dataflow.model.LogEntry;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XmlToPojoDoFnV2 extends DoFn<KV<String, byte[]>, LogEntry> {

    private static final Logger LOG = LoggerFactory.getLogger(XmlToPojoDoFnV2.class);

    @ProcessElement
    public void process(@Element KV<String, byte[]> readableFile, OutputReceiver<LogEntry> out) {
        ObjectMapper mapper = new XmlMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            mapper.readValue(readableFile.getValue(), LogEntry.class);
        } catch (IOException e) {
            LOG.error("Unable to process stream", e);
        }
    }
}