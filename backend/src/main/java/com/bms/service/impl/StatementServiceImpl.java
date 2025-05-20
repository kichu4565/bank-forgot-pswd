package com.bms.service.impl;

import com.bms.dto.StatementRequest;
import com.bms.entity.Account;
import com.bms.entity.Transaction;
import com.bms.entity.FixedDeposit;
import com.bms.exception.AccountNotFoundException;
import com.bms.exception.InvalidDateRangeException;
import com.bms.exception.PDFGenerationException;
import com.bms.repository.AccountRepository;
import com.bms.repository.TransactionRepository;
import com.bms.repository.FixedDepositRepository;
import com.bms.service.StatementService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatementServiceImpl implements StatementService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private FixedDepositRepository fixedDepositRepository;

    private static final int MAX_STATEMENT_DAYS = 90; // Maximum 3 months statement allowed
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public ByteArrayOutputStream generateStatement(String accountNumber, StatementRequest request) 
            throws AccountNotFoundException, InvalidDateRangeException, PDFGenerationException {
        
        try {
            // Validate account
            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found"));

            // Validate date range
            validateDateRange(request.getFromDate(), request.getToDate());

            // Get transactions
            LocalDateTime startDate = request.getFromDate().atStartOfDay();
            LocalDateTime endDate = request.getToDate().atTime(23, 59, 59);
            
            List<Transaction> transactions = transactionRepository
                    .findBySourceAccountNumberOrDestinationAccountNumberAndTimestampBetween(
                            accountNumber, accountNumber, startDate, endDate
                    );

            // Get FD details
            List<FixedDeposit> fixedDeposits = fixedDepositRepository
                    .findByAccount_AccountNumber(accountNumber);

            return generatePDF(account, transactions, fixedDeposits, request);
        } catch (AccountNotFoundException | InvalidDateRangeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PDFGenerationException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private void validateDateRange(LocalDate fromDate, LocalDate toDate) {
        LocalDate today = LocalDate.now();
        
        if (fromDate.isAfter(today) || toDate.isAfter(today)) {
            throw new InvalidDateRangeException("Future dates are not allowed");
        }
        
        if (fromDate.isAfter(toDate)) {
            throw new InvalidDateRangeException("From date cannot be after to date");
        }
        
        long daysBetween = fromDate.until(toDate).getDays();
        if (daysBetween > MAX_STATEMENT_DAYS) {
            throw new InvalidDateRangeException("Statement period cannot exceed " + MAX_STATEMENT_DAYS + " days");
        }
    }

    private ByteArrayOutputStream generatePDF(Account account, List<Transaction> transactions, 
            List<FixedDeposit> fixedDeposits, StatementRequest request) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            // Add header
            addHeader(document, account, request);

            // Add account summary
            addAccountSummary(document, account);

            // Add FD summary
            addFDSummary(document, fixedDeposits);

            // Add transaction table
            if (transactions.isEmpty()) {
                document.add(new Paragraph("No transactions found for the selected period."));
            } else {
                addTransactionTable(document, transactions, account);
            }

            // Add footer
            addFooter(document);

            document.close();
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
            document.close();
            throw new PDFGenerationException("Failed to generate PDF: " + e.getMessage());
        }
    }

    private void addHeader(Document document, Account account, StatementRequest request) {
        
        document.add(new Paragraph("CloudVault Bank Statement")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(18));
        

        document.add(new Paragraph("\nAccount Details:")
                .setBold());
        document.add(new Paragraph("Account Number: " + account.getAccountNumber()));
        document.add(new Paragraph("Account Holder: " + account.getCustomer().getFullName()));
        document.add(new Paragraph("Statement Period: " + 
                request.getFromDate().format(DATE_FORMATTER) + " to " + 
                request.getToDate().format(DATE_FORMATTER)));
        document.add(new Paragraph("\n"));
    }

    private void addAccountSummary(Document document, Account account) {
        document.add(new Paragraph("Account Summary:")
                .setBold());
        document.add(new Paragraph("Account Type: " + account.getAccountType()));
        document.add(new Paragraph("Current Balance: ₹" + account.getBalance()));
        document.add(new Paragraph("\n"));
    }

    private void addFDSummary(Document document, List<FixedDeposit> fixedDeposits) {
        document.add(new Paragraph("Fixed Deposit Summary:")
                .setBold());
        
        if (fixedDeposits.isEmpty()) {
            document.add(new Paragraph("No active Fixed Deposits."));
        } else {
            Table table = new Table(5);
            table.setWidth(500);

            
            table.addHeaderCell(new Cell().add(new Paragraph("FD Number")));
            table.addHeaderCell(new Cell().add(new Paragraph("Amount")));
            table.addHeaderCell(new Cell().add(new Paragraph("Interest Rate")));
            table.addHeaderCell(new Cell().add(new Paragraph("Maturity Date")));
            table.addHeaderCell(new Cell().add(new Paragraph("Status")));

            
            for (FixedDeposit fd : fixedDeposits) {
                table.addCell(new Cell().add(new Paragraph(fd.getFdAccountNumber())));
                table.addCell(new Cell().add(new Paragraph("₹" + fd.getPrincipalAmount())));
                table.addCell(new Cell().add(new Paragraph(fd.getInterestRate() + "%")));
                table.addCell(new Cell().add(new Paragraph(fd.getMaturityDate().format(DATE_FORMATTER))));
                table.addCell(new Cell().add(new Paragraph(fd.getStatus().name())));
            }

            document.add(table);
        }
        document.add(new Paragraph("\n"));
    }

    private void addTransactionTable(Document document, List<Transaction> transactions, Account account) {
        try {
            document.add(new Paragraph("Transaction History:")
                    .setBold());
            
            Table table = new Table(5);
            table.setWidth(500);

            table.addHeaderCell(new Cell().add(new Paragraph("Date")));
            table.addHeaderCell(new Cell().add(new Paragraph("Description")));
            table.addHeaderCell(new Cell().add(new Paragraph("Debit")));
            table.addHeaderCell(new Cell().add(new Paragraph("Credit")));
            table.addHeaderCell(new Cell().add(new Paragraph("Balance")));

            String currentAccountNumber = account.getAccountNumber();

            for (Transaction transaction : transactions) {
                try {
                    table.addCell(new Cell().add(new Paragraph(
                            transaction.getTimestamp().format(DATE_TIME_FORMATTER))));
                    table.addCell(new Cell().add(new Paragraph(transaction.getDescription())));
                    
                    // If the account is the source account, it's a debit
                    if (transaction.getSourceAccountNumber().equals(currentAccountNumber)) {
                        table.addCell(new Cell().add(new Paragraph("₹" + transaction.getAmount())));
                        table.addCell(new Cell().add(new Paragraph("")));
                    } else if (transaction.getDestinationAccountNumber().equals(currentAccountNumber)) {
                        // If the account is the destination account, it's a credit
                        table.addCell(new Cell().add(new Paragraph("")));
                        table.addCell(new Cell().add(new Paragraph("₹" + transaction.getAmount())));
                    }
                    
                    table.addCell(new Cell().add(new Paragraph("₹" + transaction.getSourceAccountBalance())));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PDFGenerationException("Error processing transaction: " + e.getMessage());
                }
            }

            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PDFGenerationException("Error creating transaction table: " + e.getMessage());
        }
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("\n\nGenerated on: " + 
                LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .setTextAlignment(TextAlignment.RIGHT));
        document.add(new Paragraph("This is a computer-generated statement and does not require a signature.")
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic());
    }
} 