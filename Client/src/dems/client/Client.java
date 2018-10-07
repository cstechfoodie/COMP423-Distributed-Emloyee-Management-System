package dems.client;

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import dems.api.Project;
import dems.api.RecordApi;
import dems.client.model.Logger;

public class Client {

	private static int port;

	private static String host = "localhost";

	public static void main(String[] args) {

		System.out.println("Welcome to DEMS. Please enter your Manager ID: ");
		Scanner scanner = new Scanner(System.in);

		// gear to proper server base on Manager ID
		String managerID = scanner.nextLine().trim();
		if (managerID.toUpperCase().startsWith("CA")) {
			port = 1099;
			System.out.println("in CA");
		} else if (managerID.toUpperCase().startsWith("UK")) {
			port = 2965;
			System.out.println("in UK");
		} else {
			port = 2966;
			System.out.println("in US");
		}

		Logger logger = new Logger(managerID);

		try {
			Registry registry = LocateRegistry.getRegistry(port);
			try {
				RecordApi api = (RecordApi) registry.lookup("recordApi");

				boolean loop = true;
				while (api != null && loop) {
					System.out.println("Please selection from the following options:");
					System.out.println("1 --> create Manager Record");
					System.out.println("2 --> create Employee Record");
					System.out.println("3 --> Edit Record");
					System.out.println("4 --> Check the number of records in file");
					System.out.println("5 --> Print all records to server file");
					//try {
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
							String res = api.createMRecord(firstName, lastName, employeeID, mailID, project, location);
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
							res = api.createERecord(firstName, lastName, employeeID, mailID, projectID);
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
							res = api.editRecord(recordID, fieldName, newValue);
							System.out.println(res);
							logger.logInfo(res);

							System.out.println("Do you have other operations? Y/N");
							loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
							break;
						case 4:
							res = api.getRecordCounts();
							System.out.println(res);
							logger.logInfo(res);
							System.out.println("Do you have other operations? Y/N");
							loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
							break;
						case 5:
							res = api.printData();
							System.out.println(res);
							logger.logInfo("Request server to print its data into its log.");
							System.out.println("Do you have other operations? Y/N");
							loop = scanner.nextLine().toUpperCase().equals("Y") ? true : false;
							break;
						default:
						}

//					} catch (Exception e) {
//						continue;
//					}
				}

			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			scanner.close();
			logger.close();
		}

	}

}
