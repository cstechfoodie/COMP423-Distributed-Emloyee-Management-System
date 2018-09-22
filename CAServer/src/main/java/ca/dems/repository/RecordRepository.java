package ca.dems.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ca.dems.model.Record;

public class RecordRepository implements IRecordRepository {
	
	private Map<String, List<Record>> repo = new HashMap<String, List<Record>>();

	@Override
	public boolean createMRecord(Record record) {
		try {
			String key = record.getLastName().substring(0,1).toUpperCase();
			if(repo.keySet().contains(key)) {
				repo.get(key).add(record);
			}
			else {
				List<Record> records = new ArrayList<Record>();
				records.add(record);
				repo.put(key, records);
			}
			//log here
			return true;
		}
		catch (Exception e){
			//log error here
			return false;
		}
	}

	@Override
	public void editRecord(UUID recordID, String fieldName, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int geRecordCounts() {
		int counts = 0;
		String[] keySet = (String[]) repo.keySet().toArray();
		for(int i = 0; i < keySet.length; i++) {
			counts += repo.get(keySet[i]).size();
		}
		return counts;
	}

}
