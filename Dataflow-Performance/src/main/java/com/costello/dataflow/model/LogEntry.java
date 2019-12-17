package com.costello.dataflow.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@JacksonXmlRootElement(localName = "LogEntry")
public class LogEntry implements Serializable {

    @JacksonXmlProperty(localName = "EntryId")
    private int entryId;

    @JacksonXmlProperty(localName = "LogValue")
    private String logValue;

    @JacksonXmlProperty(localName = "LogTime")
    private String logTime;

    @JacksonXmlProperty(localName = "LogProperty")
    @JacksonXmlElementWrapper(useWrapping = false)
    private String[] logProperty;

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getLogValue() {
        return logValue;
    }

    public void setLogValue(String logValue) {
        this.logValue = logValue;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String[] getLogProperty() {
        return logProperty;
    }

    public void setLogProperty(String[] logProperty) {
        this.logProperty = logProperty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntry logEntry = (LogEntry) o;
        return entryId == logEntry.entryId &&
                Objects.equals(logValue, logEntry.logValue) &&
                Objects.equals(logTime, logEntry.logTime) &&
                Arrays.equals(logProperty, logEntry.logProperty);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(entryId, logValue, logTime);
        result = 31 * result + Arrays.hashCode(logProperty);
        return result;
    }
}