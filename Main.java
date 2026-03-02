import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        MemberService memberService = new MemberService();

        while (true) {
            System.out.println("\n===== MESS MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Member");
            System.out.println("2. View Members");
            System.out.println("3. Update Member");
            System.out.println("4. Delete Member");
            System.out.println("5. Exit");
            System.out.print("Choose Option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    memberService.addMember();
                    break;
                case 2:
                    memberService.viewMembers();
                    break;
                case 3:
                    memberService.updateMember();
                    break;
                case 4:
                    memberService.deleteMember();
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Invalid Option!");
            }
        }
    }
}