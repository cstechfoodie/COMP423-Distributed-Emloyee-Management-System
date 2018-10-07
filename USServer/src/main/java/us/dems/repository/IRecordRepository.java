package us.dems.repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import us.dems.model.Record;

public interface IRecordRepository {
	public boolean createMRecord(Record record);
	public boolean createERecord(Record record);
	public boolean editRecord(String recordID, String fieldName, String newValue);
	public int getRecordCounts();
	public Map<String, List<Record>> getDataMap();
}
