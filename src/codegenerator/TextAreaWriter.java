package codegenerator;

import java.io.IOException;
import java.io.Writer;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

public final class TextAreaWriter extends Writer {

	private final JTextComponent textArea;

	public TextAreaWriter(final JTextComponent textArea) {
		this.textArea = textArea;
	}

    @Override
    public void flush(){ }
    
    @Override
    public void close(){ }

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException 
	{
		textArea.setText(textArea.getText() + new String(cbuf, off, len));
	}
}