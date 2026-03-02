import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

    LoginService loginService = new LoginService();

    if (!loginService.login()) {
        System.out.println("Access Denied!");
        return;
    }

    // Only if login successful
    Scanner sc = new Scanner(System.in);
    MemberService memberService = new MemberService();
    AttendanceService attendanceService = new AttendanceService();
    BillingService billingService = new BillingService();
    

    while (true) {
        System.out.println("\n===== MESS MANAGEMENT SYSTEM =====");
        System.out.println("1. Add Member");
        System.out.println("2. View Members");
        System.out.println("3. Update Member");
        System.out.println("4. Delete Member");
        System.out.println("5. Mark Attendance");
        System.out.println("6. Generate Monthly Bill");
        System.out.println("7. View Payments");
        System.out.println("8. Update Payment");
        System.out.println("9. Show Pending Dues");
        System.out.println("10. Exit");
        System.out.print("Choose Option: ");

        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1: memberService.addMember(); break;
            case 2: memberService.viewMembers(); break;
            case 3: memberService.updateMember(); break;
            case 4: memberService.deleteMember(); break;
            case 5: attendanceService.markAttendance(); break;
            case 6: billingService.generateMonthlyBill(); break;
            case 7: billingService.viewPayments(); break;
            case 8: billingService.updatePayment(); break;
            case 9: billingService.showPendingDues(); break;
            case 10: System.exit(0);
            default: System.out.println("Invalid Option!");
        }
    }
}
}