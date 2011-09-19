package com.predic8.plugin.membrane.dialogs.rule.composites;

import java.io.*;

import javax.xml.stream.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;

import com.predic8.membrane.core.Constants;
import com.predic8.membrane.core.rules.Rule;
import com.predic8.membrane.core.util.TextUtil;
import com.predic8.plugin.membrane.listeners.HighligtingLineStyleListner;
import com.predic8.plugin.membrane.util.SWTUtil;

public class ProxyFeaturesTabComposite extends Composite {

	private StyledText text;

	public ProxyFeaturesTabComposite(final Composite parent) {
		super(parent, SWT.NONE);
		setLayout(SWTUtil.createGridLayout(1, 10));
		this.setLayoutData(SWTUtil.getGreedyGridData());
		
		text = createStyledText();
		text.addLineStyleListener(new HighligtingLineStyleListner());
	}

	private StyledText createStyledText() {
		StyledText text = new StyledText(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		text.setLayoutData(SWTUtil.getGreedyGridData());
		return text;
	}

	public String getContent() {
		return text.getText();
	}
	
	public void setInput(Rule rule) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(baos, Constants.UTF_8);
			rule.write(writer);
			ByteArrayInputStream stream = new ByteArrayInputStream(baos.toByteArray());
			InputStreamReader reader = new InputStreamReader(stream, Constants.UTF_8);
			String xml = TextUtil.formatXML(reader); //new String(baos.toByteArray())
			text.setText(xml);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public XMLStreamReader getStreamReaderForContent() throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
	    ByteArrayInputStream stream = new ByteArrayInputStream(text.getText().getBytes());
	    return factory.createXMLStreamReader(stream);
	}
	
	@Override
	public boolean setFocus() {
		text.setFocus();
		return true;
	}
	
}