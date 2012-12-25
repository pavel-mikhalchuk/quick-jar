package com.prettybit.quickjar.test;

import com.csvreader.CsvReader;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Pavel Mikhalchuk
 */
public class BoATest {

    public static void main(String[] args) throws IOException, ParseException {
        List<Transaction> easy = sortByDate(easy());
        List<Transaction> boa = sortByDate(boa());

        Multimap<Double, Transaction> easyMap = map(easy);
        Multimap<Double, Transaction> boaMap = map(boa);

        for (Transaction transaction : easy) {
            remove(boaMap, transaction);
        }

        for (Transaction transaction : boa) {
            remove(easyMap, transaction);
        }

        System.out.println();
    }

    public static List<Transaction> easy() throws IOException, ParseException {
        List<Transaction> result = new LinkedList<Transaction>();

        CsvReader reader = new CsvReader("/home/pmikhalchuk/Desktop/checking_account_usd_oct.csv");

        while (reader.readRecord()) {
            Double amount = Double.valueOf(reader.get(2));
            String desc = reader.get(0);
            Date date = new SimpleDateFormat("dd MMM yyyy").parse(reader.get(4));

            result.add(new Transaction(amount, desc, date));
        }

        return result;
    }

    public static List<Transaction> boa() throws IOException, ParseException {
        List<Transaction> result = new LinkedList<Transaction>();

        CsvReader reader = new CsvReader("/home/pmikhalchuk/Desktop/stmt_oct.csv");

        while (reader.readRecord()) {
            Double amount = Double.valueOf(reader.get(2));
            String desc = reader.get(1);
            Date date = new SimpleDateFormat("MM/dd/yyyy").parse(reader.get(0));

            result.add(new Transaction(amount, desc, date));
        }

        return result;
    }

    private static List<Transaction> sortByDate(List<Transaction> transactions) {
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o1.date.compareTo(o2.date);
            }
        });
        return transactions;
    }

    private static Multimap<Double, Transaction> map(List<Transaction> transactions) {
        Multimap<Double, Transaction> result = HashMultimap.create();
        for (Transaction transaction : transactions) {
            result.put(transaction.amount, transaction);
        }
        return result;
    }

    private static List<Transaction> find(Double amount, List<Transaction> source) {
        List<Transaction> result = new LinkedList<Transaction>();
        for (Transaction transaction : source) {
            if (transaction.amount.equals(amount)) result.add(transaction);
        }
        return result;
    }

    private static void remove(Multimap<Double, Transaction> map, Transaction transaction) {
        Collection<Transaction> transactions = map.get(transaction.amount);
        if (transactions != null && !transactions.isEmpty()) {
            if (transactions.size() == 1) map.removeAll(transaction.amount);
            else transactions.remove(transactions.iterator().next());
        }
    }

    private static void printDuplicates(Multimap<Double, Transaction> map) {
        for (Double amount : map.keySet()) {
            if (map.get(amount).size() > 1) {
                for (Transaction transaction : map.get(amount)) {
                    System.out.println(transaction);
                }
            }
        }
    }

    private static class Transaction {
        private Double amount;
        private String desc;
        private Date date;

        private Transaction(Double amount, String desc, Date date) {
            this.amount = amount;
            this.desc = desc;
            this.date = date;
        }

        @Override
        public String toString() {
            return date + " " + amount + " " + desc;
        }
    }

}