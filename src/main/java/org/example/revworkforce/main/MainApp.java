package org.example.revworkforce.main;
import org.example.revworkforce.config.DBConnection;
import org.example.revworkforce.dao.EmployeeGoalDAO;
import org.example.revworkforce.dao.HolidayDAO;
import org.example.revworkforce.dao.LeaveDAO;
import org.example.revworkforce.dao.PerformanceDAO;
import org.example.revworkforce.model.*;
import org.example.revworkforce.service.*;
import org.example.revworkforce.util.InputUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.example.revworkforce.model.*;
import org.example.revworkforce.service.*;



public class MainApp {

    private static final String ROLE_ADMIN = "ADMIN";
    // Add this line with your other static services
    private static EmployeeGoalDAO goalDAO = new EmployeeGoalDAO();
    private static EmployeeService employeeService = new EmployeeService();
    private static LeaveService leaveService = new LeaveService();
    private static NotificationService notificationService = new NotificationService();
    private static ManagerService managerService = new ManagerService();
    private static AdminService adminService = new AdminService();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n=================================");
            System.out.println("   Welcome to RevWorkForce HRM");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Exit");

            choice = InputUtil.getInt("Choose: ");

            switch (choice) {
                case 1 -> showRoleMenu();
                case 2 -> {
                    System.out.println("Thank You for Using RevWorkForce!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid Choice!");
            }

        } while (choice != 2);
    }

    // ===================== ROLE MENU =====================
    private static void showRoleMenu() {
        System.out.println("\n=== Select Role ===");
        System.out.println("1. Employee");
        System.out.println("2. Manager");
        System.out.println("3. Admin");

        int roleChoice = InputUtil.getInt("Choose Role: ");
        String role = switch (roleChoice) {
            case 1 -> "EMPLOYEE";
            case 2 -> "MANAGER";
            case 3 -> "ADMIN";
            default -> {
                System.out.println("Invalid Role!");
                yield null;
            }
        };
        if (role == null) return;

        Employee emp = loginMenu();
        if (emp == null) {
            System.out.println("Invalid Email or Password!");
            return;
        }

        if (!emp.getRole().equalsIgnoreCase(role)) {
            System.out.println("Access Denied! Wrong Role Selected.");
            return;
        }

        System.out.println("\nLogin Successful!");
        System.out.println("Welcome " + emp.getName());
        openDashboard(emp);
    }

    // ===================== LOGIN =====================
    private static Employee loginMenu() {
        System.out.println("\n=== Login ===");
        String email = InputUtil.getString("Enter Email: ");
        String password = InputUtil.getString("Enter Password: ");
        return employeeService.login(email, password);
    }

    // ===================== DASHBOARD ROUTER =====================
    private static void openDashboard(Employee emp) {
        switch (emp.getRole()) {
            case "EMPLOYEE" -> employeeMenu(emp);
            case "MANAGER" -> managerMenu(emp);
            case "ADMIN" -> adminMenu(emp);
            default -> System.out.println("Unknown Role!");
        }
    }

private static void employeeMenu(Employee emp) {
    int choice;
    HolidayDAO holidayDAO = new HolidayDAO();
    EmployeeGoalDAO goalDAO = new EmployeeGoalDAO();

    do {
        System.out.println("\n====== Employee Dashboard ======");
        System.out.println("1. View Profile");
        System.out.println("2. Apply Leave");
        System.out.println("3. View Leave Status");
        System.out.println("4. View Notifications");
        System.out.println("5. Submit Self Review");
        System.out.println("6. View My Manager");
        System.out.println("7. View Holiday Calendar");
       System.out.println("8. Set / Update Goals");
       System.out.println("9. View My Goals");
        System.out.println("10. Change Password");
        System.out.println("11. View My Leave Balance");

        System.out.println("0. Logout");

        choice = InputUtil.getInt("Choose: ");

        switch (choice) {
            case 1 -> showProfile(emp);
            case 2 -> applyLeave(emp);
            case 3 -> viewLeaveStatus(emp);
            case 4 -> viewNotifications(emp);
            case 5 -> {
                PerformanceReview review = new PerformanceReview();


                review.setEmployeeId(emp.getEmployeeId());

                review.setReviewYear(InputUtil.getInt("Enter review year: "));
                review.setKeyDeliverables(InputUtil.getString("Enter key deliverables: "));
                review.setAccomplishments(InputUtil.getString("Enter accomplishments: "));
                review.setAreasOfImprovement(InputUtil.getString("Enter areas of improvement: "));
                review.setSelfRating(InputUtil.getInt("Enter self rating (1-5): "));

                // Set current date to avoid NullPointer in DAO
                review.setReviewDate(new java.util.Date());
                review.setStatus("PENDING");

                boolean success = employeeService.submitSelfReview(review);
                if (success) {
                    System.out.println("Self review submitted successfully!");
                } else {
                    // This prints if the DAO returns false
                    System.out.println("Failed to submit self review. Check your database connection.");
                }
            }
            case 6 -> viewMyManager(emp);

            // ===== Holiday Calendar =====
            case 7 -> {
                System.out.println("\n======= Holiday Calendar =======");
                List<Holiday> holidays = holidayDAO.getAllHolidays();
                if (holidays.isEmpty()) {
                    System.out.println("No holidays found.");
                } else {
                    for (Holiday h : holidays) {
                        System.out.println(h.getHolidayDate() + " - " + h.getHolidayName() +
                                " (" + h.getDescription() + ")");
                    }
                }
            }
            case 8 -> {
                System.out.println("\n1. Set New Goal");
                System.out.println("2. Update Existing Goal");
                int goalChoice = InputUtil.getInt("Choose: ");

                if (goalChoice == 1) {
                    // 1. Fetch the latest review ID for this employee
                    int reviewId = goalDAO.getLatestReviewId(emp.getEmployeeId());

                    // 2. Prevent goal setting if no review exists
                    if (reviewId == -1) {
                        System.out.println("No performance review found. Please submit a Self Review (Option 5) first.");
                        break;
                    }

                    EmployeeGoal goal = new EmployeeGoal();
                    goal.setEmployeeId(emp.getEmployeeId());
                    goal.setReviewId(reviewId); // CRITICAL: Assign the valid Foreign Key
                    goal.setGoalDescription(InputUtil.getString("Enter goal description: "));
                    goal.setDeadline(InputUtil.getLocalDate("Enter target date (yyyy-MM-dd): "));
                    goal.setPriority(InputUtil.getString("Enter priority (HIGH/MEDIUM/LOW): "));
                    goal.setStatus("IN_PROGRESS");

                    if (goalDAO.addGoal(goal)) {
                        System.out.println("Goal added successfully!");
                    } else {
                        System.out.println("Failed to add goal.");
                    }
                }
            }
            // ===== View My Goals =====
            case 9 -> {
                System.out.println("\n======= My Goals =======");
                List<EmployeeGoal> goals = goalDAO.getGoalsByEmployee(emp.getEmployeeId());
                if (goals.isEmpty()) {
                    System.out.println("No goals found.");
                } else {
                    for (EmployeeGoal g : goals) {
                        System.out.println(g.getGoalId() + ". " + g.getGoalDescription() +
                                " | Status: " + g.getStatus() +
                                " | Target: " + g.getTargetDate());
                    }
                }
            }
            case 10 -> {
                String newPass = InputUtil.getString("Enter new password: ");
                String confirmPass = InputUtil.getString("Confirm new password: ");

                if (newPass.equals(confirmPass)) {
                    if (employeeService.changePassword(emp.getEmployeeId(), newPass)) {
                        System.out.println("Password updated successfully!");
                    } else {
                        System.out.println("Failed to update password.");
                    }
                } else {
                    System.out.println("Passwords do not match!");
                }
            }
            case 11 -> {
                leaveService.viewMyLeaveBalance(emp.getEmployeeId());
            }

            case 0 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice!");
        }
    } while (choice != 0);
}



    private static void viewMyManager(Employee emp) {
        System.out.println("\n===== My Manager Details =====");
        int managerId = emp.getManagerId();

        if (managerId == 0) {
            System.out.println("No Manager Assigned.");
            return;
        }

        Employee manager = employeeService.getEmployee(managerId);

        if (manager == null) {
            System.out.println("Manager Not Found.");
            return;
        }

        System.out.println("ID    : " + manager.getEmployeeId());
        System.out.println("Name  : " + manager.getName());
        System.out.println("Email : " + manager.getEmail());
        System.out.println("Phone : " + manager.getPhone());
    }



    private static void managerMenu(Employee manager) {

        int choice;

        do {

            System.out.println("\n====== Manager Dashboard ======");
            System.out.println("1. View Profile");
            System.out.println("2. Apply Leave");
            System.out.println("3. View Leave Status");
            System.out.println("4. View Notifications");
            System.out.println("5. Approve / Reject Leaves");
            System.out.println("6. View Team");
            System.out.println("7. View / Add Performance Reviews");

            System.out.println("9. View Team Member Leave Balance");
            System.out.println("10. View Team Goals");
            System.out.println("0. Logout");

            choice = InputUtil.getInt("Choose: ");

            switch (choice) {

                case 1 -> showProfile(manager);

                case 2 -> applyLeave(manager);

                case 3 -> viewLeaveStatus(manager);

                case 4 -> viewNotifications(manager);

                case 5 -> approveLeaves(manager);

                case 6 -> viewTeam(manager);

                case 7 -> managePerformanceReviews(manager);



                case 9 -> viewTeamLeaveBalance(manager);

                case 10 -> viewTeamGoals(manager);

                case 0 -> System.out.println("Logging out...");

                default -> System.out.println("Invalid Choice!");
            }

        } while (choice != 0);
    }

    // ... existing imports ...

    private static void viewTeamGoals(Employee manager) {
        System.out.println("\n======= Team Goals =======");

        // Use the static goalDAO we initialized at the top of the class
        List<EmployeeGoal> teamGoals = goalDAO.getGoalsByManager(manager.getEmployeeId());

        if (teamGoals.isEmpty()) {
            System.out.println("No goals found for your team members.");
        } else {
            System.out.printf("%-15s | %-30s | %-12s | %-10s\n",
                    "Employee ID", "Goal Description", "Deadline", "Status");
            System.out.println("-------------------------------------------------------------------------");
            for (EmployeeGoal g : teamGoals) {
                System.out.printf("%-15d | %-30s | %-12s | %-10s\n",
                        g.getEmployeeId(),
                        g.getGoalDescription(),
                        g.getTargetDate(),
                        g.getStatus());
            }
        }
    }

    private static void viewTeamLeaveBalance(Employee manager) {

        LeaveDAO leaveDAO = new LeaveDAO();

        leaveDAO.getTeamLeaveBalance(manager.getEmployeeId());
    }


    public List<EmployeeGoal> getGoalsByManager(int managerId) {
        List<EmployeeGoal> goals = new ArrayList<>();
        // This query joins Employee and Goal to find all goals belonging to a specific manager's team
        String sql = """
        SELECT g.*, e.employee_id 
        FROM goal g
        JOIN performance_review pr ON g.review_id = pr.review_id
        JOIN employee e ON pr.employee_id = e.employee_id
        WHERE e.manager_id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, managerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                EmployeeGoal goal = new EmployeeGoal();
                goal.setGoalId(rs.getInt("goal_id"));
                goal.setEmployeeId(rs.getInt("employee_id"));
                goal.setGoalDescription(rs.getString("goal_description"));
                goal.setTargetDate(rs.getDate("deadline").toLocalDate());
                goal.setStatus(rs.getString("progress_status"));
                goals.add(goal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goals;
    }



    // ===================== ADMIN MENU =====================
    private static void adminMenu(Employee emp) {
        int choice;
        do {
            System.out.println("\n====== Admin Dashboard ======");
            System.out.println("1. View Profile");
            System.out.println("2. Apply Leave");
            System.out.println("3. View Leave Status");
            System.out.println("4. View Notifications");
            System.out.println("5. Approve / Reject Leaves");
            System.out.println("6. View Team");
            System.out.println("7. Add Employee");
            System.out.println("8. View All Employees");
            System.out.println("9. Deactivate Employee"); // Added per requirement
            System.out.println("10. Activate Employee");  // Added per requirement
            System.out.println("11. View All Performance Reviews");
            System.out.println("12. Add Holidays");
            System.out.println("13. DELETE Holidays");

            System.out.println("0. Logout");

            choice = InputUtil.getInt("Choose: ");
            switch (choice) {
                case 1 -> showProfile(emp);
                case 2 -> applyLeave(emp);
                case 3 -> viewLeaveStatus(emp);
                case 4 -> viewNotifications(emp);
                case 5 -> approveLeaves(emp);
                case 6 -> viewTeam(emp);
                case 7 -> addEmployee();
                case 8 -> viewAllEmployees();

                // Logic for Deactivation and Activation
                case 9 -> adminService.changeEmployeeStatus("INACTIVE");
                case 10 -> adminService.changeEmployeeStatus("ACTIVE");

                case 11 -> viewAllPerformanceReviews();
                case 12 -> adminService.addHoliday();
                case 13 -> {
                    int id = InputUtil.getInt("Enter Holiday ID to delete: ");
                    if (adminService.removeHoliday(id)) {  // ✅ use removeHoliday
                        System.out.println("Holiday deleted.");
                    } else {
                        System.out.println("Holiday ID not found.");
                    }
                }


                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid Choice!");
            }
        } while (choice != 0);
    }

    private static void viewAllPerformanceReviews() {
        PerformanceDAO performanceDAO = new PerformanceDAO();
        List<PerformanceReview> reviews = performanceDAO.getAllReviews();

        if (reviews.isEmpty()) {
            System.out.println("No performance reviews found.");
            return;
        }

        System.out.println("\n====== All Performance Reviews ======");
        System.out.printf("%-5s %-15s %-10s %-15s %-15s %-10s %-10s %-15s\n",
                "ID", "Employee ID", "Year", "Self Rating", "Manager Rating", "Status", "Reviewed By", "Review Date");

        for (PerformanceReview r : reviews) {
            System.out.printf("%-5d %-15d %-10d %-15s %-15s %-10s %-10d %-15s\n",
                    r.getReviewId(),
                    r.getEmployeeId(),
                    r.getReviewYear(),
                    r.getSelfRating() != null ? r.getSelfRating() : "-",
                    r.getManagerRating() != null ? r.getManagerRating() : "-",
                    r.getStatus(),
                    r.getReviewedBy(),
                    r.getReviewDate());
        }
    }


    // ===================== COMMON METHODS =====================
    private static void showProfile(Employee emp) {
        System.out.println("\n=== Profile ===");
        System.out.println("ID   : " + emp.getEmployeeId());
        System.out.println("Name : " + emp.getName());
        System.out.println("Email: " + emp.getEmail());
        System.out.println("Role : " + emp.getRole());
    }

    private static void applyLeave(Employee emp) {
        System.out.println("\n=== Apply Leave ===");

        List<String> leaveTypes = leaveService.getLeaveTypes();
        leaveTypes.forEach(System.out::println);

        int leaveTypeId = InputUtil.getInt("\nSelect Leave Type ID: ");
        Date fromDate;
        Date toDate;

        while (true) {
            fromDate = InputUtil.getDate("From Date (yyyy-MM-dd): ");
            toDate = InputUtil.getDate("To Date (yyyy-MM-dd): ");

            Date today = leaveService.formatDateOnly(new Date());
            if (fromDate.before(today)) {
                System.out.println("From Date cannot be in the past!");
                continue;
            }
            if (!toDate.after(fromDate)) {
                System.out.println("To Date must be after From Date!");
                continue;
            }
            break;
        }

        String reason = InputUtil.getString("Reason: ");

        LeaveApplication leave = new LeaveApplication();
        leave.setEmployeeId(emp.getEmployeeId());
        leave.setLeaveTypeId(leaveTypeId);
        leave.setFromDate(fromDate);
        leave.setToDate(toDate);
        leave.setReason(reason);

        if (leaveService.applyLeave(leave, emp.getRole())) {
            System.out.println("Leave Applied Successfully!");

            String from = leaveService.formatDate(fromDate);
            String to = leaveService.formatDate(toDate);

            Employee manager = employeeService.getEmployee(emp.getManagerId());
            if (manager != null && !ROLE_ADMIN.equalsIgnoreCase(emp.getRole())) {
                String managerMsg = emp.getName() + " has applied for leave from "
                        + from + " to " + to + ". Reason: " + reason;
                notificationService.addNotification(manager.getEmployeeId(), managerMsg, "LEAVE_REQUEST");
            }

            String employeeMsg = "Your leave from " + from + " to " + to +
                    " has been " + leave.getStatus().toLowerCase() + " successfully.";
            notificationService.addNotification(emp.getEmployeeId(), employeeMsg, "LEAVE_APPLIED");

        } else {
            System.out.println("Failed to apply leave.");
        }
    }

    private static void viewLeaveStatus(Employee emp) {
        System.out.println("\n=== Leave Status ===");
        List<LeaveApplication> list = leaveService.getEmployeeLeaves(emp.getEmployeeId());
        if (list.isEmpty()) {
            System.out.println("No Records.");
            return;
        }
        for (LeaveApplication l : list) {
            System.out.println("ID: " + l.getLeaveId() +
                    " | From: " + l.getFromDate() +
                    " | To: " + l.getToDate() +
                    " | Status: " + l.getStatus() +
                    " | Comment: " + l.getManagerComment());
        }
    }

    private static void viewNotifications(Employee emp) {
        System.out.println("\n=== Notifications ===");
        List<Notification> list = notificationService.getNotifications(emp.getEmployeeId());
        if (list.isEmpty()) {
            System.out.println("No Notifications.");
            return;
        }
        for (Notification n : list) {
            System.out.println("[" + n.getIsRead() + "] " + n.getCreatedAt() + " : " + n.getMessage());
            notificationService.markAsRead(n.getNotificationId());
        }
    }

    private static void approveLeaves(Employee emp) {
        System.out.println("\n=== Pending Leaves ===");
        List<LeaveApplication> list = leaveService.getTeamPendingLeaves(emp.getEmployeeId(), emp.getRole());

        if (list.isEmpty()) {
            System.out.println("No Pending Leaves.");
            return;
        }

        for (LeaveApplication l : list) {
            Employee requester = employeeService.getEmployee(l.getEmployeeId());
            System.out.println("Leave ID: " + l.getLeaveId());
            System.out.println("Employee: " + requester.getName());
            System.out.println("Reason  : " + l.getReason());

            String d = InputUtil.getString("Approve (A) / Reject (R): ");
            String comment = InputUtil.getString("Comment: ");
            String status = d.equalsIgnoreCase("A") ? "APPROVED" : "REJECTED";

            if (leaveService.decideLeave(l.getLeaveId(), emp.getEmployeeId(), status, comment)) {
                String msg = "Your leave from " + leaveService.formatDate(l.getFromDate()) + " to "
                        + leaveService.formatDate(l.getToDate()) + " has been " + status;
                notificationService.addNotification(l.getEmployeeId(), msg, "LEAVE_STATUS");
            }
        }
    }

    private static void viewTeam(Employee emp) {
        System.out.println("\n=== Team Members ===");
        List<Employee> list = employeeService.getTeam(emp.getEmployeeId());
        if (list.isEmpty()) {
            System.out.println("No Members.");
            return;
        }
        for (Employee e : list) {
            System.out.println("ID: " + e.getEmployeeId()
                    + " | Name: " + e.getName()
                    + " | Email: " + e.getEmail());
        }
    }
    private static void managePerformanceReviews(Employee manager) {
        // 1. Show Team
        viewTeam(manager);
        int empId = InputUtil.getInt("Enter Employee ID to view/add review: ");

        System.out.println("1. View Reviews\n2. Add Review");
        int choice = InputUtil.getInt("Choose: ");

        if (choice == 2) {
            PerformanceReview review = new PerformanceReview();
            review.setEmployeeId(empId);
            review.setReviewedBy(manager.getEmployeeId()); // Crucial: Link to manager
            review.setManagerRating(InputUtil.getInt("Enter Rating (1-5): "));
            review.setManagerComment(InputUtil.getString("Enter Comment: "));
            review.setReviewDate(new java.util.Date());
            review.setStatus("COMPLETED");
            // Ensure you also set the Review Year if your DB requires it
            review.setReviewYear(2026);

            // Call your service
            // boolean success = managerService.addPerformanceReview(review);
        }
    }

    // ===================== REVIEW EMPLOYEE (MANAGER) =====================
    private static void reviewEmployee(Employee manager) {

        int empId = InputUtil.getInt("Enter Employee ID: ");
        int year = java.time.Year.now().getValue();

        PerformanceReview r =
                managerService.getReview(empId, year);

        if (r == null) {
            System.out.println("No self-review submitted.");
            return;
        }

        System.out.println("\n--- Employee Review ---");
        System.out.println("Deliverables: " + r.getKeyDeliverables());
        System.out.println("Accomplishments: " + r.getAccomplishments());
        System.out.println("Improvements: " + r.getAreasOfImprovement());
        System.out.println("Self Rating: " + r.getSelfRating());

        r.setManagerRating(
                InputUtil.getInt("Manager Rating (1-5): ")
        );

        r.setManagerFeedback(
                InputUtil.getString("Feedback: ")
        );

        r.setReviewedBy(manager.getEmployeeId());
        r.setStatus("COMPLETED");
        r.setReviewDate(new java.util.Date());

        boolean ok = managerService.updateReview(r);

        if (ok)
            System.out.println("Review Completed!");
        else
            System.out.println("Update Failed!");
    }



    private static void addEmployee() {

        System.out.println("\n=== Add Employee ===");

        Employee emp = new Employee();

        emp.setName(InputUtil.getString("Enter Name: "));
        emp.setEmail(InputUtil.getString("Enter Email: "));
        emp.setPassword(InputUtil.getString("Enter Password: "));
        emp.setPhone(InputUtil.getString("Enter Phone: "));
        emp.setAddress(InputUtil.getString("Enter Address: "));
        emp.setDob(InputUtil.getDate("Enter DOB (yyyy-MM-dd): "));
        emp.setJoiningDate(
                InputUtil.getDate("Enter Joining Date (yyyy-MM-dd): ")
        );
        emp.setAlternatePhone(
                InputUtil.getString("Enter Alternate Phone: ")
        );

        // ================= Department =================
        List<Department> departments = employeeService.getAllDepartments();

        System.out.println("\n--- Departments ---");
        for (Department d : departments) {
            System.out.println(d.getDepartmentId() + ". " + d.getDepartmentName());
        }

        emp.setDepartmentId(InputUtil.getInt("Select Department ID: "));


        // ================= Designation (FIX) =================
        List<Designation> designations = employeeService.getAllDesignations();

        System.out.println("\n--- Designations ---");
        for (Designation d : designations) {
            System.out.println(d.getDesignationId() + ". " + d.getDesignationName());
        }

        emp.setDesignationId(InputUtil.getInt("Select Designation ID: "));


        // ================= Manager =================
        List<Employee> managers = employeeService.getAllManagers();

        System.out.println("\n--- Managers ---");
        for (Employee m : managers) {
            System.out.println(m.getEmployeeId() + ". " + m.getName());
        }

        emp.setManagerId(InputUtil.getInt("Select Manager ID: "));


        // ================= Role =================
        System.out.println("Role Options: EMPLOYEE, MANAGER, ADMIN");
        emp.setRole(InputUtil.getString("Enter Role: ").toUpperCase());


        // ================= Save =================
        if (employeeService.addEmployee(emp)) {
            System.out.println("Employee added successfully!");
        } else {
            System.out.println("Failed to add employee.");
        }
    }


//    // ===================== VIEW ALL EMPLOYEES =====================

private static void viewAllEmployees() {
    System.out.println("\n==================== System Employees ====================");
    List<Employee> list = employeeService.getAllEmployees();

    if (list.isEmpty()) {
        System.out.println("No employees found.");
    } else {
        System.out.printf("%-5s | %-20s | %-25s | %-10s | %-10s\n",
                "ID", "Name", "Email", "Role", "Status");
        System.out.println("--------------------------------------------------------------------------");
        for (Employee e : list) {
            System.out.printf("%-5d | %-20s | %-25s | %-10s | %-10s\n",
                    e.getEmployeeId(), e.getName(), e.getEmail(), e.getRole(), e.getStatus());
        }
    }
}
    private static HolidayService holidayService = new HolidayService();

    private static void viewHolidays() {
        System.out.println("\n=== Company Holidays ===");
        List<Holiday> holidays = holidayService.getAllHolidays();

        if (holidays.isEmpty()) {
            System.out.println("No holidays found.");
            return;
        }

        System.out.printf("%-5s %-20s %-15s\n", "ID", "Holiday Name", "Date");
        for (Holiday h : holidays) {
            System.out.printf("%-5d %-20s %-15s\n",
                    h.getHolidayId(),
                    h.getHolidayName(),
                    h.getHolidayDate());
        }
    }

}