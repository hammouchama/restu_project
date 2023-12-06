package com.cofe.cafe_server.service;

import com.cofe.cafe_server.JWT.JwtFilter;
import com.cofe.cafe_server.POJO.Bill;
import com.cofe.cafe_server.constents.CafeConstants;
import com.cofe.cafe_server.repository.BillRepository;
import com.cofe.cafe_server.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.itextpdf.text.FontFactory.getFont;

@Service
@AllArgsConstructor
public class BillService {
    BillRepository billRepository;
    JwtFilter jwtFilter;

    public ResponseEntity<String> generatRaport(Map<String, Object> requestMap) {
        try {
            String fileName;
            if (validateRequestMap(requestMap)){

                if(requestMap.containsKey("isGenerate") && !(Boolean)requestMap.get("Generate")){
                    fileName=(String) requestMap.get("uuid");
                }else {
                    fileName=CafeUtils.getUUID();
                    requestMap.put("uuid",fileName);
                    insertBill(requestMap);
                }
                createReport(requestMap,fileName);
                return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
            }
            return  CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);

        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void createReport(Map<String, Object> requestMap, String fileName) throws FileNotFoundException, DocumentException, JSONException {
        String data="Name: "+requestMap.get("name")+"\n"+
                "Contact Number: "+requestMap.get("contactNumber")+"\n"+
                "Email: "+requestMap.get("email")+"\n"+
                "Payment Method: "+requestMap.get("paymentMethod")+"\n";
        Document documented =new Document();
        PdfWriter.getInstance(documented,new FileOutputStream(CafeConstants.STORE_LOCATION+"\\"+fileName+".pdf"));
         documented.open();
         setRectangleInPdf(documented);

        Paragraph chunk=new Paragraph("Cafe Management System",getFont("Header"));
        chunk.setAlignment(Element.ALIGN_CENTER);
        documented.add(chunk);
        Paragraph paragraph =new Paragraph(data+"\n \n ",getFont("Data"));
         documented.add(paragraph);

        PdfPTable pdfPTable=new PdfPTable(5);

        pdfPTable.setWidthPercentage(100);

        addTableHedar(pdfPTable);

        JSONArray jsonArray=CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
        for (int i=0;i<jsonArray.length();i++){
            addRow(pdfPTable,CafeUtils.getMapFromJSON(jsonArray.getString(i)));
        }
        documented.add(pdfPTable);

        Paragraph footer =new Paragraph("Total : "+requestMap.get("totalAmount")+"\n" +
                "Thank you for visiting . Please visit again!!",getFont("Data"));
        documented.add(footer);
        documented.close();
    }

    private void addRow(PdfPTable pdfPTable, Map<String, Object> mapFromJSON) {
        pdfPTable.addCell((String) mapFromJSON.get("name"));
        pdfPTable.addCell((String) mapFromJSON.get("category"));
        pdfPTable.addCell((String) mapFromJSON.get("quantity"));
        pdfPTable.addCell(Double.toString((Double) mapFromJSON.get("price")));
        pdfPTable.addCell(Double.toString((double) mapFromJSON.get("total")));
    }

    private void addTableHedar(PdfPTable pdfPTable) {
        Stream.of("Name","Category","Quantity","Price","Sub Total")
                .forEach(columnTitle->{
                    PdfPCell header=new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(columnTitle);
                });
    }

    private void setRectangleInPdf(Document documented) throws  DocumentException{
        Rectangle rectangle=new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        documented.add(rectangle);
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill=new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethode((String) requestMap.get("paymentMethod"));
            bill.setTotal(Float.parseFloat((String) requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepository.save(bill);
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber")&&
                requestMap.containsKey("email")&&
                requestMap.containsKey("paymentMethod")&&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }

    public ResponseEntity<List<Bill>> getBills() {
        try {
            if (jwtFilter.isAdmin()){
                return new ResponseEntity<>(billRepository.findAll(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(billRepository.findByEmail(jwtFilter.getCurrentUser()),HttpStatus.OK);
            }
        }catch (Exception ex){
         ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
     try {
          byte[] bytArray=new byte[0];
          if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)){
              return  new ResponseEntity<>(bytArray,HttpStatus.BAD_REQUEST);
          }
          String filePath=CafeConstants.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
           if (CafeUtils.isFileExist(filePath)){
              bytArray=getByteArray(filePath);
              return new ResponseEntity<>(bytArray,HttpStatus.OK);
           }else {
               requestMap.put("isGenerate",false);
               generatRaport(requestMap);
               bytArray =getByteArray(filePath);
               return new ResponseEntity<>(bytArray,HttpStatus.OK);
           }
     }catch (Exception e){
         e.printStackTrace();
     }
     return null;
    }

    private byte[] getByteArray(String filePath) throws IOException {
        File initalFile=new File(filePath);
        InputStream targetStream =new FileInputStream(initalFile);
        byte[] byteArray= IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    public ResponseEntity<String> dalete(long id) {
        try {
            Optional<Bill> optionalBill=billRepository.findById(id);
            if (!optionalBill.isEmpty()){
                billRepository.deleteById(id);
                return CafeUtils.getResponseEntity("Bill Deleted Successfully",HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Bill id does not exist ",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.STORE_LOCATION,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
