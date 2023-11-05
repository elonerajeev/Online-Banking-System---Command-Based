import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Account {
    private final String accountNumber; // Unique identifier for the account
    private final String accountHolder; // Name of the account holder
    private double balance; // Current balance in the account
    private final String password; // Password for the account

    public Account(String accountNumber, String accountHolder, double initialBalance, String password) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.password = password;
    }

    public String getAccountNumber() {
        return accountNumber; // Get the account number
    }

    public String getAccountHolder() {
        return accountHolder; // Get the account holder's name
    }

    public double getBalance() {
        return balance; // Get the current balance
    }

    public boolean verifyPassword(String inputPassword) {
        return password.equals(inputPassword); // Verify if the given password matches the account's password
    }

    public void deposit(double amount) {
        balance += amount; // Add the given amount to the balance
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount; // Subtract the amount from the balance if sufficient funds are available
            return true; // Return true if withdrawal is successful
        }
        return false; // Return false if there's insufficient balance
    }

    public boolean transfer(Account target, double amount) {
        if (withdraw(amount)) {
            target.deposit(amount); // Transfer the given amount to another account
            return true; // Return true if transfer is successful
        }
        return false; // Return false if there's insufficient balance
    }
}

class Bank {
    private final Map<String, Account> accounts; // Map to store accounts using their account numbers as keys
    private final Map<String, TransactionHistory> transactionHistories; // Map to store transaction histories for
                                                                        // accounts

    public Bank() {
        accounts = new HashMap<>(); // Initialize the accounts map
        transactionHistories = new HashMap<>(); // Initialize the transaction histories map
    }

    public void createAccount(String accountNumber, String accountHolder, double initialBalance, String password) {
        if (!accounts.containsKey(accountNumber)) {
            Account account = new Account(accountNumber, accountHolder, initialBalance, password);
            accounts.put(accountNumber, account); // Add a new account to the accounts map
            transactionHistories.put(accountNumber, new TransactionHistory()); // Initialize transaction history for the
                                                                               // new account
            System.out.println("Account created successfully.");
        } else {
            System.out.println("Account already exists."); // Display a message if the account already exists
        }
    }

    // Method to display details of all accounts
    public void printAllAccounts() {
        System.out.println("All Accounts:");
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            String accountNumber = entry.getKey(); // Retrieve the account number
            Account account = entry.getValue(); // Retrieve the account details
            System.out.println("Account Number: " + accountNumber + ", Account Holder: " + account.getAccountHolder()); // Display
                                                                                                                        // each
                                                                                                                        // account
                                                                                                                        // number
                                                                                                                        // with
                                                                                                                        // its
                                                                                                                        // associated
                                                                                                                        // account
                                                                                                                        // holder's
                                                                                                                        // name
        }
    }

    // Method to deposit money into an account
    public void deposit(String accountNumber, double amount) {
        if (accounts.containsKey(accountNumber)) {
            Account account = accounts.get(accountNumber); // Retrieve the account based on the account number
            account.deposit(amount); // Deposit the specified amount into the account
            transactionHistories.get(accountNumber).addTransaction("Deposit", amount);
            // Add a 'Deposit' transaction to the account's transaction history
            System.out.println("Deposit successful. New balance: " + account.getBalance());
            // Display the new balance after the deposit
        } else {
            System.out.println("Account not found.");
            // Display a message if the account is not found
        }
    }

    // Method to withdraw money from an account
    public void withdraw(String accountNumber, double amount) {
        if (accounts.containsKey(accountNumber)) {
            Account account = accounts.get(accountNumber); // Retrieve the account based on the account number
            if (account.withdraw(amount)) {
                transactionHistories.get(accountNumber).addTransaction("Withdrawal", amount);
                // Add a 'Withdrawal' transaction to the account's transaction history
                System.out.println("Withdrawal successful. New balance: " + account.getBalance());
                // Display the new balance after the withdrawal
            } else {
                System.out.println("Insufficient balance.");
                // Display a message if the withdrawal amount exceeds the available balance
            }
        } else {
            System.out.println("Account not found.");
            // Display a message if the account is not found
        }
    }

    // Method to transfer money between two accounts
    public void transfer(String fromAccount, String toAccount, double amount) {
        if (accounts.containsKey(fromAccount) && accounts.containsKey(toAccount)) {
            Account sourceAccount = accounts.get(fromAccount); // Retrieve the source account
            Account targetAccount = accounts.get(toAccount); // Retrieve the target account
            if (sourceAccount.transfer(targetAccount, amount)) {
                transactionHistories.get(fromAccount).addTransaction("Transfer to " + toAccount, amount);
                transactionHistories.get(toAccount).addTransaction("Transfer from " + fromAccount, amount);
                // Add 'Transfer' transactions to the respective accounts' transaction histories
                System.out.println(
                        "Transfer successful. New balance in " + fromAccount + ": " + sourceAccount.getBalance());
                // Display the new balance in the source account after the transfer
                System.out.println("New balance in " + toAccount + ": " + targetAccount.getBalance());
                // Display the new balance in the target account after the transfer
            } else {
                System.out.println("Transfer failed. Insufficient balance in the source account.");
                // Display a message if the transfer fails due to insufficient balance in the
                // source account
            }
        } else {
            System.out.println("One or both accounts not found.");
            // Display a message if either or both of the accounts are not found
        }
    }

    public double checkBalance(String accountNumber) {
        // Method to check the balance of an account
        if (accounts.containsKey(accountNumber)) {
            return accounts.get(accountNumber).getBalance(); // Return the balance of the specified account
        } else {
            System.out.println("Account not found.");
            return 0; // Return 0 if the account is not found
        }
    }

    public List<String> getTransactionHistory(String accountNumber) {
        // Method to retrieve transaction history for an account
        if (transactionHistories.containsKey(accountNumber)) {
            return transactionHistories.get(accountNumber).getTransactionHistory();
            // Return the transaction history of the specified account
        } else {
            System.out.println("Account not found.");
            return new ArrayList<>();
            // Return an empty list if the account is not found
        }
    }
}

class TransactionHistory {
    private final List<String> transactions; // List to store transaction details

    public TransactionHistory() {
        transactions = new ArrayList<>(); // Initialize the list for transactions
    }

    public void addTransaction(String transactionType, double amount) {
        transactions.add(transactionType + ": " + amount); // Add a new transaction to the list
    }

    public List<String> getTransactionHistory() {
        return transactions; // Get the transaction history for an accoun
    }
}

public class OnlineBankingSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        // Main menu and functionality to interact with the banking system
        while (true) {
            System.out.println("Welcome to the Online Banking App!");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. View Transaction History");
            System.out.println("7. Print All Accounts");
            System.out.println("8. Exit");
            System.out.print("Please select an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    System.out.print("Enter account holder name: ");
                    String accountHolder = scanner.nextLine();
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    System.out.print("Set a password: ");
                    String password = scanner.nextLine();
                    bank.createAccount(accountNumber, accountHolder, initialBalance, password);
                    break;
                case 2:
                    // Deposit implementation
                    System.out.print("Enter account number: ");
                    String depositAccountNumber = scanner.nextLine();
                    System.out.print("Enter the amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    bank.deposit(depositAccountNumber, depositAmount);
                    break;

                case 3:
                    // Withdraw implementation
                    System.out.print("Enter account number: ");
                    String withdrawAccountNumber = scanner.nextLine();
                    System.out.print("Enter the amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    bank.withdraw(withdrawAccountNumber, withdrawAmount);
                    break;

                case 4:
                    // Transfer implementation
                    System.out.print("Enter source account number: ");
                    String fromAccount = scanner.nextLine();
                    System.out.print("Enter target account number: ");
                    String toAccount = scanner.nextLine();
                    System.out.print("Enter the amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine(); // Consume the newline
                    bank.transfer(fromAccount, toAccount, transferAmount);
                    break;
                case 5:
                    // Check Balance implementation
                    System.out.print("Enter account number: ");
                    String checkBalanceAccountNumber = scanner.nextLine();
                    double balance = bank.checkBalance(checkBalanceAccountNumber);
                    System.out.println("Current balance: " + balance);
                    break;
                case 6:
                    // View Transaction History implementation
                    System.out.print("Enter account number: ");
                    String historyAccountNumber = scanner.nextLine();
                    List<String> transactions = bank.getTransactionHistory(historyAccountNumber);
                    if (!transactions.isEmpty()) {
                        System.out.println("Transaction History:");
                        for (String transaction : transactions) {
                            System.out.println(transaction);
                        }
                    }
                    break;
                case 7:
                    // Print All Accounts implementation
                    bank.printAllAccounts(); // Display all existing accounts
                    break;
                case 8:
                    System.out.println("Thank you for using the Online Banking App!");
                    scanner.close();
                    System.exit(0); // Exit the application
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

// ###################################### OUTPUT
// ###################################
// PS C:\Users\Rajeev kumar> cd "c:\Users\Rajeev kumar\OneDrive\Desktop\Java
// Exam\" ; if ($?) { javac OnlineBankingSystem.java } ; if ($?) { java
// OnlineBankingSystem }
// Welcome to the Online Banking App!
// 1. Create Account
// 2. Deposit
// 3. Withdraw
// 4. Transfer
// 5. Check Balance
// 6. View Transaction History
// 7. Print All Accounts
// 8. Exit

// Please select an option: 1
// Enter account number: 6299546899
// Enter account holder name: Rajeev Kumar
// Enter initial balance: 1000
// Set a password: 123
// Account created successfully.
// Welcome to the Online Banking App!
// _____________________________________________________
// 1. Create Account
// 2. Deposit
// 3. Withdraw
// 4. Transfer
// 5. Check Balance
// 6. View Transaction History
// 7. Print All Accounts
// 8. Exit

// Please select an option: 1
// Enter account number: 9304660152
// Enter account holder name: Elone Rajeev
// Enter initial balance: 1000
// Set a password: 321
// Account created successfully.
// Welcome to the Online Banking App!
// _____________________________________________________
// 1. Create Account
// 2. Deposit
// 3. Withdraw
// 4. Transfer
// 5. Check Balance
// 6. View Transaction History
// 7. Print All Accounts
// 8. Exit

// Please select an option: 1
// Enter account number: 9264144212
// Enter account holder name: Ellison
// Enter initial balance: 1000
// Set a password: 123321
// Account created successfully.
// Welcome to the Online Banking App!
// _____________________________________________________
// 1. Create Account
// 2. Deposit
// 3. Withdraw
// 4. Transfer
// 5. Check Balance
// 6. View Transaction History
// 7. Print All Accounts
// 8. Exit

// Please select an option: 7
// All Accounts:
// Account Number: 9304660152, Account Holder: Elone Rajeev
// Account Number: 6299546899, Account Holder: Rajeev Kumar
// Account Number: 9264144212, Account Holder: Ellison
// Welcome to the Online Banking App!
// _____________________________________________________
