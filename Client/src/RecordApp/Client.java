package RecordApp;

import java.util.Scanner;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import RecordApp.RecordPackage.Project;
import dems.client.model.Logger;

public class Client extends Thread{

	private int port;

	private String host = "localhost";
	
	private String managerID;
	
	public Client(String managerID) {
		this.managerID = managerID;
	}
	
	private static Record recordController;
	
	@Override
	public void run() {
	     // create and initialize the ORB
	     ORB orb = ORB.init();

	     // get the root naming context
	     org.omg.CORBA.Object objRef = null;
		try {
			objRef = orb.resolve_initial_references("NameService");
		} catch (org.omg.CORBA.ORBPackage.InvalidName e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	     // Use NamingContextExt instead of NamingContext. This is 
	     // part of the Interoperable naming Service.  
	     NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
	     
		String name = "";
		if (managerID.toUpperCase().startsWith("CA")) {
			name = "CARecord";
			System.out.println("in CA");
		} else if (managerID.toUpperCase().startsWith("UK")) {
			name = "UKRecord";
			System.out.println("in UK");
		} else {
			name = "USRecord";
			System.out.println("in US");
		}
		
		Logger logger = new Logger(managerID);
		logger.logInfo("Manager with ID: [" + managerID + "] logged in.");
		
		 // resolve the Object Reference in Naming
	    try {
			recordController = (Record)RecordHelper.narrow(ncRef.resolve_str(name));
		} catch (NotFound e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (CannotProceed e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidName e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			for(int i = 0; i < 10; i++) {
				logger.logCreateMRecord("Shunyu", "Wang", 12345 + i, "shunyu.wang@mail.concordia.ca", new Project("P00001", "client", "project"), "CA");
				String res = recordController.createMRecord(managerID, "Shunyu", "Wang", 12345 + i, "shunyu.wang@mail.concordia.ca", new Project("P00001", "client", "project"), "CA");
				logger.logInfo(res);
			}
			
			for(int i = 0; i < 10; i++) {
				logger.logCreateERecord("Shunyu", "Zang", 67890 + i, "shunyu.wang@mail.concordia.ca", "P00001"+i);
				String res = recordController.createERecord(managerID, "Shunyu", "Zang", 67890 + i, "shunyu.wang@mail.concordia.ca", "P00001" + i);
				logger.logInfo(res);
			}
			
//			for(int i = 0; i < 5; i++) {
//				logger.logCreateERecord("Martin", "Smith", 20100 + i, "aaa.bbb@mail.concordia.ca", "P00001"+i);
//				String res = api.createERecord("Martin", "Smith", 20100 + i, "aaa.bbb@mail.concordia.ca", "P00001" + i);
//				logger.logInfo(res);
//			}
			
			String res = recordController.printData();
			logger.logInfo(res);
			String count = recordController.getRecordCounts(managerID);
			logger.logInfo(count);
			for(int i = 0; i < 10; i++) {
				logger.logEdit("ER10001", "projectID", "P00200");
				res = recordController.editRecord(managerID, "ER10001", "projectID", "P00200");
				logger.logInfo(res);
				logger.logEdit("MR10003", "location", "US");
				res = recordController.editRecord(managerID, "MR10003", "location", "US");
				logger.logInfo(res);
				logger.logEdit("MR10005", "mailID", "john@mail.concordia.ca");
				res = recordController.editRecord(managerID, "MR10005", "mailID", "john@mail.concordia.ca");	
				logger.logInfo(res);
			}
			res = recordController.printData();
			logger.logInfo(res);
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String args[])
	 {
	   try{
	     // create and initialize the ORB
	     ORB orb = ORB.init(args, null);

	     // get the root naming context
	     org.omg.CORBA.Object objRef = 
	         orb.resolve_initial_references("NameService");
	     // Use NamingContextExt instead of NamingContext. This is 
	     // part of the Interoperable naming Service.  
	     NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

	     //System.out.println("Obtained a handle on server object: " + recordController);
	     
		System.out.println("Welcome to DEMS. Please enter your Manager ID: ");
		Scanner scanner = new Scanner(System.in);
		String managerID = scanner.nextLine().trim();
		
		String name = "";
		if (managerID.toUpperCase().startsWith("CA")) {
			name = "CARecord";
			System.out.println("in CA");
		} else if (managerID.toUpperCase().startsWith("UK")) {
			name = "UKRecord";
			System.out.println("in UK");
		} else {
			name = "USRecord";
			System.out.println("in US");
		}
		
		Logger logger = new Logger(managerID);
		logger.logInfo("Manager with ID: [" + managerID + "] logged in.");
		
		 // resolve the Object Reference in Naming
	     recordController = (Record)RecordHelper.narrow(ncRef.resolve_str(name));
	     
		boolean loop = true;
		while (recordController != null && loop) {
			System.out.println("Please selection from the following options:");
			System.out.println("1 --> create Manager Record");
			System.out.println("2 --> create Employee Record");
			System.out.println("3 --> Edit Record");
			System.out.println("4 --> Check the number of records in file");
			System.out.println("5 --> Transfer record");
			System.out.println("6 --> Print all records to server file");
			try {
				int option = Integer.parseInt(scanner.nextLine().trim());
				switch (option) {
				case 1:
					System.out.println("Please enter the manager detail:");
					System.out.println("First Name:");
					String firstName = scanner.nextLine();

					System.out.println("Last Name:");
					String lastName = scanner.nextLine();

					System.out.println("Employee ID:");
					Integer employeeID = Integer.parseInt(scanner.nextLine().trim());

					System.out.println("MailID:");
					String mailID = scanner.nextLine();
					System.out.println("Location(CA, US, UK):");
					String location = scanner.nextLine();

					System.out.println("Project ID:");
					String projectID = scanner.nextLine();
					System.out.println("Client Name:");
					String clientName = scanner.nextLine();
					System.out.println("Project Name:");
					String projectName = scanner.nextLine();

					Project project = new Project(projectID, clientName, projectName);
					logger.logCreateMRecord(firstName, lastName, employeeID, mailID, project, location);
					String res = recordController.createMRecord(managerID, firstName, lastName, employeeID, mailID, project, location);
					System.out.println(res);
					logger.logInfo(res);

					System.out.println("Do you have other operations? Y/N");
					loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
					break;
				case 2:
					System.out.println("Please enter the employee detail:");
					System.out.println("First Name:");
					firstName = scanner.nextLine();

					System.out.println("Last Name:");
					lastName = scanner.nextLine();

					System.out.println("Employee ID:");
					employeeID = Integer.parseInt(scanner.nextLine().trim());

					System.out.println("MailID:");
					mailID = scanner.nextLine();

					System.out.println("Project ID:");
					projectID = scanner.nextLine();
					logger.logCreateERecord(firstName, lastName, employeeID, mailID, projectID);
					res = recordController.createERecord(managerID, firstName, lastName, employeeID, mailID, projectID);
					System.out.println(res);
					logger.logInfo(res);
					System.out.println("Do you have other operations? Y/N");
					loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
					break;
				case 3:
					System.out.println("Record ID:");
					String recordID = scanner.nextLine();

					System.out.println("Field Name");
					String fieldName = scanner.nextLine();

					System.out.println("Give a new value:");
					String newValue = scanner.nextLine();

					logger.logEdit(recordID, fieldName, newValue);
					res = recordController.editRecord(managerID, recordID, fieldName, newValue);
					System.out.println(res);
					logger.logInfo(res);

					System.out.println("Do you have other operations? Y/N");
					loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
					break;
				case 4:
					res = recordController.getRecordCounts(managerID);
					System.out.println(res);
					logger.logInfo(res);
					System.out.println("Do you have other operations? Y/N");
					loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
					break;
				case 5:
					System.out.println("Record ID:");
					recordID = scanner.nextLine();

					System.out.println("remoteCenterServerName:");
					String remoteCenterServerName = scanner.nextLine();
					
					res = recordController.transferRecord(managerID, recordID, remoteCenterServerName);

					logger.logInfo(res);
					System.out.println(res);
					System.out.println("Do you have other operations? Y/N");
					loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
					break;
				case 6:
					res = recordController.printData();
					System.out.println(res);
					logger.logInfo("Request server to print its data into its log.");
					System.out.println("Do you have other operations? Y/N");
					loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
					break;
				default:
				}
			} catch(Exception e) {
				continue;
			}
		}

	     } catch (Exception e) {
	       System.out.println("ERROR : " + e) ;
	       e.printStackTrace(System.out);
	       }
	 }

	

}
