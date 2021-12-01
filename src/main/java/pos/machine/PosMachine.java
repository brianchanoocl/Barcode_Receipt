package pos.machine;

import java.util.ArrayList;
import java.util.List;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        System.out.println("function starts");



        List<String> digestedBarcodes = getUniqueBarcode(barcodes);
        int[] quantityList = getUniqueBarcodeQuantity(digestedBarcodes, barcodes);


        List<ItemInfo> productInfoList = getProductNameAndPrice(digestedBarcodes);
        return generateReceipt(productInfoList, quantityList);
    }

    private List<String> getUniqueBarcode(List<String> barcodes) {
        List<String> digestedBarcodes = new ArrayList<>();
        boolean repeated;
        for(String item : barcodes){
            if( digestedBarcodes.size() < 1 )
                digestedBarcodes.add(item);
            else{
                repeated = false;
                for (String digestedBarcode : digestedBarcodes) {
                    if (digestedBarcode.equals(item)) {
                        repeated = true;
                        break;
                    }
                }
                if(!repeated)
                    digestedBarcodes.add(item);
            }
        }
        return digestedBarcodes;
    }


    private int[] getUniqueBarcodeQuantity(List<String> digestedBarcodes, List<String> barcodes) {
        int[] quantityList = new int[digestedBarcodes.size()];
        for(String item : barcodes){
            for (int i=0 ; i<digestedBarcodes.size() ; i++) {
                if( digestedBarcodes.get(i).equals(item) )
                    quantityList[i]++;
            }
        }


        return quantityList;
    }

    private List<ItemInfo> getProductNameAndPrice(List<String> barcodes) {
        List<ItemInfo> productInfoList = new ArrayList<>();
        for(String item : barcodes){
            ItemInfo product = new ItemInfo( item, fetchNameByBarcode(item), fetchPriceByBarcode(item));
            productInfoList.add(product);
        }
        return productInfoList;
    }

    private String fetchNameByBarcode(String barcode) {
        String productName = "";
        List<ItemInfo> database = ItemDataLoader.loadAllItemInfos();
        for (ItemInfo item : database) {
            if (item.getBarcode().equals(barcode))
                productName = item.getName();
        }
        return productName;
    }

    private int fetchPriceByBarcode(String barcode) {
        int productPrice = 0;
        List<ItemInfo> database = ItemDataLoader.loadAllItemInfos();
        for (ItemInfo item : database) {
            if (item.getBarcode().equals(barcode))
                productPrice = item.getPrice();
        }
        return productPrice;
    }

    private String generateReceipt(List<ItemInfo> productInfoList, int[] quantityList) {
        String receipt = "***<store earning no money>Receipt***\n";
        int total = 0;
        for (int i=0 ; i<quantityList.length ; i++) {
            System.out.println(quantityList[i]);
            receipt += generateReceiptLine(productInfoList.get(i), quantityList[i]);
            total += fetchSubtotal(productInfoList.get(i))*quantityList[i];
        }
        receipt += "----------------------\n";
        receipt += "Total: " + total + " (yuan)\n";
        receipt += "**********************";


        return receipt;
    }

    private String generateReceiptLine(ItemInfo productInfoListItem, int quantity) {
        return "Name: " + productInfoListItem.getName() + ", Quantity: " + quantity + ", Unit price: " + productInfoListItem.getPrice() + " (yuan), Subtotal: " + quantity*productInfoListItem.getPrice() + " (yuan)\n";
    }

    private int fetchSubtotal(ItemInfo productInfoListItem) {
        return productInfoListItem.getPrice();
    }

}
