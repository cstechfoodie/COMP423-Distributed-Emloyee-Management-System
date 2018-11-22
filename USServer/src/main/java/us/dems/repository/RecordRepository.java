package us.dems.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dems.api.EmployeeRecord;
import dems.api.ManagerRecord;
import dems.api.Record;
import us.dems.model.Logger;


public class RecordRepository implements IRecordRepository {
	
	private Logger logger = new Logger();

	private Map<String, List<Record>> repo = new HashMap<String, List<Record>>();
	
	public RecordRepository() {	}
	
	public RecordRepository(Map<String, List<Record>> repo) {
		this.repo = repo;
	}

	/*
	 * (non-Javadoc)
	 * @see ca.dems.repository.IRecordRepository#createMRecord(ca.dems.model.Record)
	 */
	@Override
	public synchronized boolean createMRecord(Record record) {
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
	public synchronized boolean createERecord(Record record) {
		return createMRecord(record);
	}
	
	/*
	 * (non-Javadoc)
	 * @see ca.dems.repository.IRecordRepository#editRecord(java.util.UUID, java.lang.String, java.lang.Object)
	 */
	@Override
	public synchronized boolean editRecord(String recordID, String fieldName, String newValue) {
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
		Collection<List<Record>> allLists = repo.values();
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
							break;
						case "US":
							m.setLocation("US");
							break;
						case "UK":
							m.setLocation("UK");
							break;
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
							m.getProject().setProjectName(newValue);
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
							m.getProject().setClientName(newValue);
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
						if(r.getClass().getSimpleName().equalsIgnoreCase("EmployeeRecord")) {
							EmployeeRecord e = (EmployeeRecord) r;
							e.setProjectID(newValue.toString());
						}	
						else {
							ManagerRecord m = (ManagerRecord) r;
							m.getProject().setProjectID(newValue);
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
	public synchronized int getRecordCounts() {
		int counts = 0;
		Object[] keySet = repo.keySet().toArray();
		for (int i = 0; i < keySet.length; i++) {
			counts += repo.get(keySet[i]).size();
		}
		return counts;
	}

	@Override
	public synchronized Map<String, List<Record>> getDataMap() {
		return this.repo;
	}
	
	@Override
	public synchronized boolean isExisted(String recordID) {
		Collection<List<Record>> allLists = repo.values();
		for (List<Record> lst : allLists) {
			for (Record r : lst) {
				if (r.getRecordID().toString().equals(recordID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public synchronized Record getRecord(String recordID) {
		Collection<List<Record>> allLists = repo.values();
		for (List<Record> lst : allLists) {
			for (Record r : lst) {
				if (r.getRecordID().toString().equals(recordID)) {
					return r;
				}
			}
		}
		return null;
	}
	
	@Override
	public synchronized boolean deleteRecord(String recordID) {
		Collection<List<Record>> allLists = repo.values();
		for (List<Record> lst : allLists) {
			for (Record r : lst) {
				if (r.getRecordID().toString().equals(recordID)) {
					lst.remove(r);
					return true;
				}
			}
		}
		return false;
	}

}
