package ca.dems.repository;

import java.util.UUID;

import ca.dems.model.Record;

public interface IRecordRepository {
	public boolean createMRecord(Record record);
	public boolean editRecord(UUID recordID, String fieldName, Object newValue);
	public int geRecordCounts();
}
