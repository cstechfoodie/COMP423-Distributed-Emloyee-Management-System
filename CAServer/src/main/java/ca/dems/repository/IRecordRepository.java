package ca.dems.repository;

import java.util.UUID;

import ca.dems.model.Record;

public interface IRecordRepository {
	public boolean createMRecord(Record record);
	public boolean createERecord(Record record);
	public boolean editRecord(String recordID, String fieldName, String newValue);
	public int getRecordCounts();
}
