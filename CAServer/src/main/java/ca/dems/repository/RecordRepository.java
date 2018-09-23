package ca.dems.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import ca.dems.model.EmployeeRecord;
import ca.dems.model.Location;
import ca.dems.model.ManagerRecord;
import ca.dems.model.Project;
import ca.dems.model.Record;

public class RecordRepository implements IRecordRepository {

	private Map<String, List<Record>> repo = new ConcurrentHashMap<String, List<Record>>();

	/*
	 * (non-Javadoc)
	 * @see ca.dems.repository.IRecordRepository#createMRecord(ca.dems.model.Record)
	 */
	@Override
	public boolean createMRecord(Record record) {
		try {
			String key = record.getLastName().substring(0, 1).toUpperCase();
			if (repo.keySet().contains(key)) {
				repo.get(key).add(record);
			} else {
				List<Record> records = new ArrayList<Record>();
				records.add(record);
				repo.put(key, records);
			}
			// log here
			return true;
		} catch (Exception e) {
			// log error here
			return false;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see ca.dems.repository.IRecordRepository#editRecord(java.util.UUID, java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean editRecord(UUID recordID, String fieldName, Object newValue) {
		if (newValue == null) {
			return false;
		}
		if (fieldName.equals("location")) {
			if (!newValue.toString().equalsIgnoreCase(Location.CA.toString())
					&& !newValue.toString().equalsIgnoreCase(Location.CA.toString())
					&& !newValue.toString().equalsIgnoreCase(Location.UK.toString())) {
				return false;
			}
		}
		List<List<Record>> allLists = (List<List<Record>>) repo.values();
		boolean foundAndSet = false;

		switch (fieldName) {
		case "mailID":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().equals(recordID)) {
						r.setMailID(newValue.toString());
						foundAndSet = true;
						break;
					}
				}
				if (foundAndSet) {
					break;
				}
			}
			if (foundAndSet) {
				return true;
			} else {
				return false;
			}
		case "location":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().equals(recordID)) {
						ManagerRecord m = (ManagerRecord) r;
						switch (newValue.toString()) {
						case "CA":
							m.setLocation(Location.CA);
						case "US":
							m.setLocation(Location.US);
						case "UK":
							m.setLocation(Location.UK);
						}
						foundAndSet = true;
						break;
					}
				}
				if (foundAndSet) {
					break;
				}
			}
			if (foundAndSet) {
				return true;
			} else {
				return false;
			}
		case "projects":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().equals(recordID)) {
						ManagerRecord m = (ManagerRecord) r;
						try {
							m.setProjects((List<Project>) newValue);
							foundAndSet = true;
							break;
						} catch (Exception e) {
							// log error
							break;
						}
					}
				}
				if (foundAndSet) {
					break;
				}
			}
			if (foundAndSet) {
				return true;
			} else {
				return false;
			}
		case "projectID":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().equals(recordID)) {
						EmployeeRecord e = (EmployeeRecord) r;
						e.setProjectID(newValue.toString());
						foundAndSet = true;
						break;
					}
				}
				if (foundAndSet) {
					break;
				}
			}
			if (foundAndSet) {
				return true;
			} else {
				return false;
			}

		default:
			// log error - fieldName error
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see ca.dems.repository.IRecordRepository#geRecordCounts()
	 */
	@Override
	public int geRecordCounts() {
		int counts = 0;
		String[] keySet = (String[]) repo.keySet().toArray();
		for (int i = 0; i < keySet.length; i++) {
			counts += repo.get(keySet[i]).size();
		}
		return counts;
	}

}
