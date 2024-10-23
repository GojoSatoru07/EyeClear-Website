import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Prescription {
    private int prescID;
    private String firstName;
    private String lastName;
    private String address;
    private float sphere;
    private float axis;
    private float cylinder;
    private Date examinationDate;
    private String optometrist;

    private final String[] remarkTypes = {"Client", "Optometrist"};
    private final ArrayList<String> postRemarkTypes = new ArrayList<>();

    // Method to add a prescription to a file if it meets all conditions
    public boolean addPrescription() {
        ArrayList<String> errors = new ArrayList<>();

        if (!isValidName(firstName)) {
            errors.add("Invalid first name. Must be 4-15 characters and start with an uppercase letter.");
        }
        if (!isValidName(lastName)) {
            errors.add("Invalid last name. Must be 4-15 characters and start with an uppercase letter.");
        }
        if (address == null || address.length() < 20) {
            errors.add("Invalid address. Must be at least 20 characters long.");
        }
        if (!isValidSphere(sphere)) {
            errors.add("Invalid sphere value. Must be between -20.00 and 20.00.");
        }
        if (!isValidCylinder(cylinder)) {
            errors.add("Invalid cylinder value. Must be between -4.00 and 4.00.");
        }
        if (!isValidAxis(axis)) {
            errors.add("Invalid axis value. Must be between 0 and 180.");
        }
        if (optometrist == null || optometrist.length() < 8 || optometrist.length() > 25) {
            errors.add("Invalid optometrist name. Length must be 8-25 characters.");
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                System.out.println(error);
            }
            return false;
        }

        // Write the prescription to presc.txt if all validations pass
        try (FileWriter writer = new FileWriter("presc.txt", true)) {
            writer.write(String.format(
                "ID: %d, Name: %s %s, Address: %s, Sphere: %.2f, Cylinder: %.2f, Axis: %.2f, Date: %s, Optometrist: %s\n",
                prescID, firstName, lastName, address, sphere, cylinder, axis,
                new SimpleDateFormat("dd/MM/yyyy").format(examinationDate), optometrist));
            System.out.println("Prescription added to presc.txt.");
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to presc.txt: " + e.getMessage());
            return false;
        }
    }

    // Method to add a remark with validation
    public boolean addRemark(String remark, String category) {
        ArrayList<String> errors = new ArrayList<>();
        String[] words = remark.trim().split("\\s+");
        
        if (words.length < 6 || words.length > 20 || !Character.isUpperCase(remark.trim().charAt(0))) {
            errors.add("Invalid remark. Must be 6-20 words and start with an uppercase letter.");
        }
        if (!isValidCategory(category)) {
            errors.add("Invalid category. Must be 'Client' or 'Optometrist'.");
        }
        if (postRemarkTypes.contains(category)) {
            errors.add("Duplicate category. Each category can only be used once.");
        }
        if (postRemarkTypes.size() >= 2) {
            errors.add("Maximum number of remark categories reached. You can only add up to 2.");
        }

        if (!errors.isEmpty()) {
            for (String error : errors) {
                System.out.println(error);
            }
            return false;
        }

        try (FileWriter writer = new FileWriter("remark.txt", true)) {
            writer.write(String.format("Remark: %s, Category: %s\n", remark, category));
            System.out.println("Remark added to remark.txt.");
            postRemarkTypes.add(category);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to remark.txt: " + e.getMessage());
            return false;
        }
    }

    // Helper methods for validation
    private boolean isValidName(String name) {
        return name != null && name.length() >= 4 && name.length() <= 15 && Character.isUpperCase(name.charAt(0));
    }

    private boolean isValidSphere(float sphere) {
        return sphere >= -20.00 && sphere <= 20.00;
    }

    private boolean isValidCylinder(float cylinder) {
        return cylinder >= -4.00 && cylinder <= 4.00;
    }

    private boolean isValidAxis(float axis) {
        return axis >= 0 && axis <= 180;
    }

    private boolean isValidCategory(String category) {
        for (String type : remarkTypes) {
            if (type.equalsIgnoreCase(category)) return true;
        }
        return false;
    }

    // Setters to initialize prescription details (for testing purposes)
    public void setDetails(int id, String firstName, String lastName, String address,
                           float sphere, float axis, float cylinder, Date date, String optometrist) {
        this.prescID = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.sphere = sphere;
        this.axis = axis;
        this.cylinder = cylinder;
        this.examinationDate = date;
        this.optometrist = optometrist;
    }

    // Main method to run the program
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Prescription p = new Prescription();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dateFormat.setLenient(false);

            System.out.print("Enter Prescription ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter Address: ");
            String address = scanner.nextLine();

            System.out.print("Enter Sphere: ");
            float sphere = scanner.nextFloat();

            System.out.print("Enter Cylinder: ");
            float cylinder = scanner.nextFloat();

            System.out.print("Enter Axis: ");
            float axis = scanner.nextFloat();
            scanner.nextLine();

            System.out.print("Enter Examination Date (DD/MM/YYYY): ");
            String dateStr = scanner.nextLine();
            Date examinationDate = dateFormat.parse(dateStr);

            System.out.print("Enter Optometrist Name: ");
            String optometrist = scanner.nextLine();

            p.setDetails(id, firstName, lastName, address, sphere, axis, cylinder, examinationDate, optometrist);

            if (p.addPrescription()) {
                System.out.println("Prescription added successfully!");
            } else {
                System.out.println("Failed to add prescription.");
            }

            System.out.print("Enter Remark: ");
            String remark = scanner.nextLine();

            System.out.print("Enter Remark Category (Client/Optometrist): ");
            String category = scanner.nextLine();

            if (p.addRemark(remark, category)) {
                System.out.println("Remark added successfully!");
            } else {
                System.out.println("Failed to add remark.");
            }
        } catch (ParseException e) {
            System.err.println("Invalid date format. Please enter in DD/MM/YYYY format.");
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}

// TestPrescription.java
class TestPrescription {
    public static void main(String[] args) {
        // Running test cases for addPrescription
        System.out.println("=== Testing addPrescription ===");
        runAddPrescriptionTestCase(1, "Alice", "Peter", "1/60 Roberts St, VI, 3012", 2.50f, -1.75f, 90f,
                "23/10/2024", "Dr. Williams"); // Valid case

        runAddPrescriptionTestCase(2, "Joe", "Zared", "13/201 Auburn Rd, VIC, 3122", 0.0f, 0.0f, 0f,
                "11/12/2024", "Dr. Alan"); // Valid case

        runAddPrescriptionTestCase(3, "Bob", "Smith", "123 Example St, VIC, 3000", -21.0f, 1.5f, 60f,
                "30/10/2024", "Dr. John"); // Invalid sphere

        runAddPrescriptionTestCase(4, "Charlie", "Brown", "456 Sample Ave, VIC, 3001", 19.0f, 5.0f, 180f,
                "05/11/2024", "Dr. Jane"); // Invalid cylinder

        runAddPrescriptionTestCase(5, "David", "Williams", "789 Test Rd, VIC, 3002", 5.5f, -1.0f, 90f,
                "15/11/2024", "Dr. Smith"); // Valid case

        runAddPrescriptionTestCase(6, "Eve", "Davis", "999 Sample Blvd, VIC, 3003", 10.0f, 0.0f, 181f,
                "20/11/2024", "Dr. Richards"); // Invalid axis

        // Running test cases for addRemark
        System.out.println("=== Testing addRemark ===");
        runAddRemarkTestCase(1, "The doctor was friendly", "Optometrist"); // Valid remark
        runAddRemarkTestCase(2, "the doctor was friendly", "Client"); // Invalid remark (lowercase)
        runAddRemarkTestCase(3, "This is a great service", "Client"); // Valid remark
        runAddRemarkTestCase(4, "Excellent service but a bit slow", "Optometrist"); // Valid remark
        runAddRemarkTestCase(5, "Friendly staff", "Optometrist"); // Valid remark
        runAddRemarkTestCase(6, "Amazing experience", "Doctor"); // Invalid category
    }

    private static void runAddPrescriptionTestCase(int testID, String firstName, String lastName,
                                                    String address, float sphere, float cylinder,
                                                    float axis, String examinationDateStr,
                                                    String optometrist) {
        Prescription prescription = new Prescription();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date examinationDate = dateFormat.parse(examinationDateStr);

            // Set the details
            prescription.setDetails(testID, firstName, lastName, address,
                    sphere, axis, cylinder, examinationDate, optometrist);

            // Attempt to add the prescription
            System.out.println("Prescription Test Case " + testID + ": Adding prescription");
            if (prescription.addPrescription()) {
                System.out.println("Prescription added successfully!");
            } else {
                System.out.println("Failed to add prescription.");
            }
        } catch (ParseException e) {
            System.err.println("Invalid date format in Test Case " + testID + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred in Test Case " + testID + ": " + e.getMessage());
        }

        System.out.println(); // Add a blank line for better readability
    }

    private static void runAddRemarkTestCase(int testID, String remark, String category) {
        Prescription prescription = new Prescription();
        System.out.println("Remark Test Case " + testID + ": Adding remark");

        // Attempt to add the remark
        if (prescription.addRemark(remark, category)) {
            System.out.println("Remark added successfully!");
        } else {
            System.out.println("Failed to add remark.");
        }

        System.out.println(); 
    }
}
