package org.bonitasoft.connector.watermark.pdf;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.connector.ConnectorException;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 *The connector execution will follow the steps
 * 1 - setInputParameters() --> the connector receives input parameters values
 * 2 - validateInputParameters() --> the connector can validate input parameters values
 * 3 - connect() --> the connector can establish a connection to a remote server (if necessary)
 * 4 - executeBusinessLogic() --> execute the connector
 * 5 - getOutputParameters() --> output are retrieved from connector
 * 6 - disconnect() --> the connector can close connection to remote server (if any)
 */
public class PDFWatermark extends AbstractPDFWatermark {

	@Override
	protected void executeBusinessLogic() throws ConnectorException {
		PdfReader reader = null;
		PdfStamper stamp = null;
		File tmpFile = null;
		try{
			Document pdfDocument = getAPIAccessor().getProcessAPI().getLastDocument(getExecutionContext().getProcessInstanceId(),getPdfDocumentId());
			byte[] inputByte = getAPIAccessor().getProcessAPI().getDocumentContent(pdfDocument.getContentStorageId()); 
			reader = new PdfReader(inputByte);
			int n = reader.getNumberOfPages();
			tmpFile = File.createTempFile("pdfWaterMarked", "") ;
			stamp = new PdfStamper(reader, new FileOutputStream(tmpFile));
			int i = 0;
			PdfContentByte under;
			String watermarkImageId = getWatermarkImageId();
			Image img = null;
			if(watermarkImageId != null && !watermarkImageId.isEmpty()){
				Document imageDoc = getAPIAccessor().getProcessAPI().getLastDocument(getExecutionContext().getProcessInstanceId(),watermarkImageId);
				img = Image.getInstance(getAPIAccessor().getProcessAPI().getDocumentContent(imageDoc.getContentStorageId()));
			}else{ //Use sample
				URL imgURL = PDFWatermark.class.getClassLoader().getResource("sample.png");
				img = Image.getInstance(imgURL);
			}
			img.setAbsolutePosition(getXPosition(), getYPosition());
			
			while (i < n) { // Add on each pages
				i++;
				under = stamp.getUnderContent(i);
				under.addImage(img);
			}
		}catch(Exception e){
			throw new ConnectorException(e);
		}finally{
			
			if(stamp != null){
				try {
					stamp.close();
				} catch (Exception e) {
					throw new ConnectorException(e);
				}
			}
			if(reader != null){
				reader.close();
			}
		}

		FileInputStream fis =   null ;
		try{
			fis = new FileInputStream(tmpFile) ;
			byte[] outputByte = new byte[fis.available()] ;
			fis.read(outputByte) ;
			fis.close() ;
			Document newDoc = getAPIAccessor().getProcessAPI().getLastDocument(getExecutionContext().getProcessInstanceId(),getOutputDocument());
			if(newDoc!= null){
				getAPIAccessor().getProcessAPI().attachNewDocumentVersion(getExecutionContext().getProcessInstanceId(),getOutputDocument(),getOutputDocument()+".pdf","application/pdf",outputByte) ;
			}else{
				getAPIAccessor().getProcessAPI().attachDocument(getExecutionContext().getProcessInstanceId(),getOutputDocument(),getOutputDocument()+".pdf","application/pdf",outputByte) ;
			}
			
		}catch(Exception e){
			throw new ConnectorException(e);
		}finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					throw new ConnectorException(e);
				}
			}
			if(tmpFile != null && tmpFile.exists()){
				tmpFile.delete();
			}
		}

	}

	@Override
	public void connect() throws ConnectorException{
		//[Optional] Open a connection to remote server

	}

	@Override
	public void disconnect() throws ConnectorException{
		//[Optional] Close connection to remote server

	}

}
