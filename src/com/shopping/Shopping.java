package com.shopping;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Category {
    BOOK, FOOD, DRINK, CLOTHING, OTHER
}

record Item(String name, Category category, double price, boolean isClearance) {
}

interface DiscountStrategy {
    double calculateDiscount(Item item);
}

class BasicDiscountStrategy implements DiscountStrategy {
    @Override
    public double calculateDiscount(Item item) {
        return switch (item.category()) {
            case BOOK, FOOD, DRINK -> 0.05;
            case CLOTHING -> 0.20;
            default -> 0.03;
        };
    }
}

class ClearanceDiscount implements DiscountStrategy {
    @Override
    public double calculateDiscount(Item item) {
        double basicDiscount = new BasicDiscountStrategy().calculateDiscount(item);
        return basicDiscount + (basicDiscount) * 0.20;
    }
}

class Receipt {
    private final List<Item> items;
    private double totalCost;
    private double totalSavings;

    public Receipt(List<Item> items) {
        this.items = items;
        calculateTotal();
    }

    private void calculateTotal() {
        totalCost = 0;
        totalSavings = 0;
        DiscountStrategy basicDiscountStrategy = new BasicDiscountStrategy();
        DiscountStrategy ClearanceDiscount = new ClearanceDiscount();

        for (Item item : items) {
            double discountRate = item.isClearance() ? ClearanceDiscount.calculateDiscount(item) : basicDiscountStrategy.calculateDiscount(item);
            double discount = item.price() * discountRate;
            double finalPrice = item.price() - discount;

            totalCost += finalPrice;
            totalSavings += discount;
        }

        totalCost = Math.round(totalCost * 100.0) / 100.0;
        totalSavings = Math.round(totalSavings * 100.0) / 100.0;
    }

    public void printReceipt() {
        System.out.println("Receipt:");
        for (Item item : items) {
            double discountRate = item.isClearance() ? new ClearanceDiscount().calculateDiscount(item) : new BasicDiscountStrategy().calculateDiscount(item);
            double discount = item.price() * discountRate;
            double finalPrice = item.price() - discount;
            System.out.printf("%s: $%.2f\n", item.name(), finalPrice);
        }
        System.out.printf("Total: $%.2f\n", totalCost);
        System.out.printf("You saved: $%.2f\n", totalSavings);
    }
}

public class Shopping {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the number of items:");
            int numberOfItems = Integer.parseInt(scanner.nextLine());
            List<Item> items = new ArrayList<>();
            int inputCountIncrement = 1;
            for (int i = 0; i < numberOfItems; i++) {
                System.out.println(inputCountIncrement +" Enter item details (name, category[BOOK/FOOD/DRINK/CLOTHING], price, clearance[true,false]) separated by commas:");
                System.out.println(inputCountIncrement +" Example Input below 4 Params: 1 book,BOOK,14.49,true");
                String[] itemDetails = scanner.nextLine().split(",");
                String name = itemDetails[0].trim();
                Category category = Category.valueOf(itemDetails[1].trim().toUpperCase());
                double price = Double.parseDouble(itemDetails[2].trim());
                boolean isClearance = Boolean.parseBoolean(itemDetails[3].trim());
                items.add(new Item(name, category, price, isClearance));
                inputCountIncrement++;
            }

            Receipt receipt = new Receipt(items);
            receipt.printReceipt();
        }catch(Exception e){
            System.out.println("Please Enter Valid Details. Program Existed");
        }

    }
}
