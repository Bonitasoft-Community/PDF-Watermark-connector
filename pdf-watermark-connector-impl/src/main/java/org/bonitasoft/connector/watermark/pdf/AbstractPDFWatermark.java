package org.bonitasoft.connector.watermark.pdf;


import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

public abstract class AbstractPDFWatermark extends AbstractConnector {

	protected final static String PDFDOCUMENTID_INPUT_PARAMETER = "pdfDocumentId";
	protected final static String WATERMARKIMAGEID_INPUT_PARAMETER = "watermarkImageId";
	protected final static String XPOSITION_INPUT_PARAMETER = "xPosition";
	protected final static String YPOSITION_INPUT_PARAMETER = "yPosition";
	protected final static String OUTPUTDOCUMENT_INPUT_PARAMETER = "outputDocument";

	protected final java.lang.String getPdfDocumentId() {
		return (java.lang.String) getInputParameter(PDFDOCUMENTID_INPUT_PARAMETER);
	}

	protected final java.lang.String getWatermarkImageId() {
		return (java.lang.String) getInputParameter(WATERMARKIMAGEID_INPUT_PARAMETER);
	}

	protected final java.lang.Float getXPosition() {
		return (java.lang.Float) getInputParameter(XPOSITION_INPUT_PARAMETER);
	}

	protected final java.lang.Float getYPosition() {
		return (java.lang.Float) getInputParameter(YPOSITION_INPUT_PARAMETER);
	}

	protected final java.lang.String getOutputDocument() {
		return (java.lang.String) getInputParameter(OUTPUTDOCUMENT_INPUT_PARAMETER);
	}
	
	@Override
	public void validateInputParameters() throws ConnectorValidationException {
		try {
			getPdfDocumentId();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"pdfDocumentId type is invalid");
		}
		try {
			getWatermarkImageId();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"watermarkImageId type is invalid");
		}
		try {
			getXPosition();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("xPosition type is invalid");
		}
		try {
			getYPosition();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("yPosition type is invalid");
		}
		try {
			getOutputDocument();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"outputDocument type is invalid");
		}

	}

}
