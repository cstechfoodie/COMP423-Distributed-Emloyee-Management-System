package ca.dems.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ca.dems.model.EmployeeRecord;
import ca.dems.model.Logger;
import ca.dems.model.ManagerRecord;
import ca.dems.model.Record;

public class RecordRepository implements IRecordRepository {
	
	private Logger logger = new Logger();

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
	
	@Override
	public boolean createERecord(Record record) {
		return createMRecord(record);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ca.dems.repository.IRecordRepository#editRecord(java.util.UUID, java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean editRecord(String recordID, String fieldName, String newValue) {
		if (newValue == null) {
			return false;
		}
		if (fieldName.equals("location")) {
			if (!newValue.toString().equalsIgnoreCase("CA")
					&& !newValue.toString().equalsIgnoreCase("US")
					&& !newValue.toString().equalsIgnoreCase("UK")) {
				return false;
			}
		}
		List<List<Record>> allLists = (List<List<Record>>) repo.values();
		boolean foundAndSet = false;

		switch (fieldName) {
		case "mailID":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().toString().equals(recordID)) {
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
					if (r.getRecordID().toString().equals(recordID)) {
						ManagerRecord m = (ManagerRecord) r;
						switch (newValue.toString()) {
						case "CA":
							m.setLocation("CA");
						case "US":
							m.setLocation("US");
						case "UK":
							m.setLocation("UK");
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
		case "projectName":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().toString().equals(recordID)) {
						ManagerRecord m = (ManagerRecord) r;
						try {
							m.getProject().projectName = newValue;
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
		case "clientName":
			for (List<Record> lst : allLists) {
				for (Record r : lst) {
					if (r.getRecordID().toString().equals(recordID)) {
						ManagerRecord m = (ManagerRecord) r;
						try {
							m.getProject().clientName = newValue;
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
					if (r.getRecordID().toString().equals(recordID)) {
						if(r.getClass().getName().equalsIgnoreCase("EmployeeRecord")) {
							EmployeeRecord e = (EmployeeRecord) r;
							e.setProjectID(newValue.toString());
						}	
						else {
							ManagerRecord m = (ManagerRecord) r;
							m.getProject().projectID = newValue.toString();
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
	public int getRecordCounts() {
		int counts = 0;
		String[] keySet = (String[]) repo.keySet().toArray();
		for (int i = 0; i < keySet.length; i++) {
			counts += repo.get(keySet[i]).size();
		}
		return counts;
	}

	@Override
	public boolean printData() {
		try {
			logger.logMap(this.repo);
			return true;
		}
		catch (Exception e){
			System.out.println("Logger Error!");
			return false;
		}
	}

}
