// File: BankingSystem.java
// Compile: javac BankingSystem.java
// Run: java BankingSystem


import java.util.Scanner;

// Abstract base class: Abstraction + Encapsulation
abstract class Account {
    private String accountNumber;
    private String holderName;
    protected double balance; // Protected so subclasses can access

    // Constructor
    public Account(String accountNumber, String holderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialDeposit;
    }

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    // Abstract methods: must be implemented by subclasses
    public abstract void deposit(double amount);
    public abstract void withdraw(double amount);

    // Display account info
    public void displayAccountInfo() {
        System.out.println("----------------------------------");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + holderName);
        System.out.println("Current Balance: ₹" + balance);
        System.out.println("----------------------------------");
    }
}

// Derived class 1: Savings Account
class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accNo, String name, double deposit, double interestRate) {
        super(accNo, name, deposit);
        this.interestRate = interestRate;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited ₹" + amount + " successfully!");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn ₹" + amount + " successfully!");
        } else {
            System.out.println("Insufficient balance or invalid amount!");
        }
    }

    public void applyInterest() {
        double interest = balance * (interestRate / 100);
        balance += interest;
        System.out.println("Interest ₹" + interest + " applied successfully!");
    }
}

// Derived class 2: Checking Account
class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accNo, String name, double deposit, double overdraftLimit) {
        super(accNo, name, deposit);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited ₹" + amount + " successfully!");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance + overdraftLimit) {
            balance -= amount;
            System.out.println("Withdrawn ₹" + amount + " successfully!");
        } else {
            System.out.println("Withdrawal exceeds overdraft limit or invalid amount!");
        }
    }
}

// Transaction class
class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public void printTransaction() {
        System.out.println(type + ": ₹" + amount);
    }
}

// Bank system (composition + polymorphism)
public class BankingSystem {
    private static Scanner sc = new Scanner(System.in);
    private static Account[] accounts = new Account[100];
    private static Transaction[][] transactions = new Transaction[100][50];
    private static int accountCount = 0;
    private static int[] transactionCount = new int[100];

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n====== BANKING SYSTEM MENU ======");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. View Account Balance");
            System.out.println("5. Transfer Funds");
            System.out.println("6. View Transaction History");
            System.out.println("7. Apply Interest (Savings Only)");
            System.out.println("8. Display All Accounts");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: createAccount(); break;
                case 2: depositMoney(); break;
                case 3: withdrawMoney(); break;
                case 4: viewBalance(); break;
                case 5: transferFunds(); break;
                case 6: viewTransactions(); break;
                case 7: applyInterest(); break;
                case 8: displayAllAccounts(); break;
                case 9: System.out.println("Thank you for using the banking system!"); break;
                default: System.out.println("Invalid choice. Try again!");
            }
        } while (choice != 9);
    }

    private static void createAccount() {
        System.out.println("Select Account Type: ");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
        int type = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Account Holder Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter Initial Deposit: ₹");
        double deposit = sc.nextDouble();

        if (type == 1) {
            System.out.print("Enter Interest Rate (%): ");
            double rate = sc.nextDouble();
            accounts[accountCount] = new SavingsAccount(accNo, name, deposit, rate);
        } else if (type == 2) {
            System.out.print("Enter Overdraft Limit: ₹");
            double limit = sc.nextDouble();
            accounts[accountCount] = new CheckingAccount(accNo, name, deposit, limit);
        } else {
            System.out.println("Invalid account type!");
            return;
        }

        System.out.println("Account created successfully!");
        accountCount++;
    }

    private static Account findAccount(String accNo) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accNo)) {
                return accounts[i];
            }
        }
        return null;
    }

    private static int getAccountIndex(String accNo) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber().equals(accNo)) {
                return i;
            }
        }
        return -1;
    }

    private static void depositMoney() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter Amount to Deposit: ₹");
            double amount = sc.nextDouble();
            acc.deposit(amount);
            int idx = getAccountIndex(accNo);
            transactions[idx][transactionCount[idx]++] = new Transaction("Deposit", amount);
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void withdrawMoney() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc != null) {
            System.out.print("Enter Amount to Withdraw: ₹");
            double amount = sc.nextDouble();
            acc.withdraw(amount);
            int idx = getAccountIndex(accNo);
            transactions[idx][transactionCount[idx]++] = new Transaction("Withdraw", amount);
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void viewBalance() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc != null) {
            acc.displayAccountInfo();
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void transferFunds() {
        System.out.print("Enter Source Account Number: ");
        String srcAcc = sc.nextLine();
        System.out.print("Enter Destination Account Number: ");
        String destAcc = sc.nextLine();
        System.out.print("Enter Amount to Transfer: ₹");
        double amount = sc.nextDouble();

        Account from = findAccount(srcAcc);
        Account to = findAccount(destAcc);

        if (from != null && to != null) {
            if (from.getBalance() >= amount) {
                from.withdraw(amount);
                to.deposit(amount);
                int srcIdx = getAccountIndex(srcAcc);
                int destIdx = getAccountIndex(destAcc);
                transactions[srcIdx][transactionCount[srcIdx]++] = new Transaction("Transfer Out", amount);
                transactions[destIdx][transactionCount[destIdx]++] = new Transaction("Transfer In", amount);
                System.out.println("Funds transferred successfully!");
            } else {
                System.out.println("Insufficient funds!");
            }
        } else {
            System.out.println("One or both accounts not found!");
        }
    }

    private static void viewTransactions() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        int idx = getAccountIndex(accNo);
        if (idx != -1) {
            System.out.println("Transaction History for Account " + accNo + ":");
            for (int i = 0; i < transactionCount[idx]; i++) {
                transactions[idx][i].printTransaction();
            }
        } else {
            System.out.println("Account not found!");
        }
    }

    private static void applyInterest() {
        System.out.print("Enter Account Number: ");
        String accNo = sc.nextLine();
        Account acc = findAccount(accNo);
        if (acc != null && acc instanceof SavingsAccount) {
            ((SavingsAccount) acc).applyInterest();
        } else {
            System.out.println("Invalid account or not a savings account!");
        }
    }

    private static void displayAllAccounts() {
        if (accountCount == 0) {
            System.out.println("No accounts available!");
            return;
        }
        for (int i = 0; i < accountCount; i++) {
            accounts[i].displayAccountInfo();
        }
    }
}
