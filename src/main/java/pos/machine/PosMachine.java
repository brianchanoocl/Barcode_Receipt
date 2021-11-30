package pos.machine;

import java.util.ArrayList;
import java.util.List;

class ReceiptItem {
    private final String name;
    private final int quantity;
    private final int price;


    public ReceiptItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        System.out.println("function starts");



        List<String> digestedBarcodes = digestBarcodes(barcodes);
        int[] quantityList = digestBarcodesQuantity(digestedBarcodes, barcodes);


        List<ItemInfo> shoppingCart = new ArrayList<>();
        shoppingCart = gatherItemInfo(digestedBarcodes);
        String receipt = generateReceipt(shoppingCart, quantityList);
        return receipt;
    }

    private List<String> digestBarcodes(List<String> barcodes) {
        List<String> digestedBarcodes = new ArrayList<>();
        Boolean repeated = false;
        for(String item : barcodes){
            if( digestedBarcodes.size() < 1 )
                digestedBarcodes.add(item);
            else{
                repeated = false;
                for (int i=0 ; i<digestedBarcodes.size() ; i++) {
                    if( digestedBarcodes.get(i).equals(item) )
                        repeated = true;
                }
                if(!repeated)
                    digestedBarcodes.add(item);
            }
        }
        return digestedBarcodes;
    }


    private int[] digestBarcodesQuantity(List<String> digestedBarcodes, List<String> barcodes) {
        int[] quantityList = new int[digestedBarcodes.size()];
        for(String item : barcodes){
            for (int i=0 ; i<digestedBarcodes.size() ; i++) {
                if( digestedBarcodes.get(i).equals(item) )
                    quantityList[i]++;
            }
        }


        return quantityList;
    }



    private List<ItemInfo> gatherItemInfo(List<String> barcodes) {
        List<ItemInfo> shoppingCart = new ArrayList<>();
        String name = "";
        int price = 0;
        for(String item : barcodes){
            ItemInfo product = new ItemInfo( item, fetchNameByBarcode(item), fetchPriceByBarcode(item));
            shoppingCart.add(product);
        }
        return shoppingCart;
    }


    private String fetchNameByBarcode(String barcode) {
        String productName = new String();
        List<ItemInfo> database = ItemDataLoader.loadAllItemInfos();
        for (int i=0 ; i<database.size() ; i++) {
            ItemInfo item = database.get(i);
            if(item.getBarcode().equals(barcode))
                productName = item.getName();
        }
        return productName;
    }


    private int fetchPriceByBarcode(String barcode) {
        int productPrice = 0;
        List<ItemInfo> database = ItemDataLoader.loadAllItemInfos();
        for (int i=0 ; i<database.size() ; i++) {
            ItemInfo item = database.get(i);
            if(item.getBarcode().equals(barcode))
                productPrice = item.getPrice();
        }
        return productPrice;
    }


    private String generateReceipt(List<ItemInfo> shoppingCart, int[] quantityList) {
        String receipt = "***<store earning no money>Receipt***\n";
        int total = 0;
        for (int i=0 ; i<quantityList.length ; i++) {
            System.out.println(quantityList[i]);
            receipt += generateReceiptLine(shoppingCart.get(i), quantityList[i]);
            total += fetchSubtotal(shoppingCart.get(i))*quantityList[i];
        }
        receipt += "----------------------\n";
        receipt += "Total: " + total + " (yuan)\n";
        receipt += "**********************";


        return receipt;
    }


    private String generateReceiptLine(ItemInfo shoppingCartItem, int quantity) {
        String line = "Name: " + shoppingCartItem.getName() + ", Quantity: " + quantity + ", Unit price: " + shoppingCartItem.getPrice() + " (yuan), Subtotal: " + quantity*shoppingCartItem.getPrice() + " (yuan)\n";
        return line;
    }


    private int fetchSubtotal(ItemInfo shoppingCartItem) {
        return shoppingCartItem.getPrice();
    }


}
