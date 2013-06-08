package general;

import java.util.Hashtable;
import javax.swing.JOptionPane;
import language.LanguageEntry;
import language.LanguageGenerator;

public class DisplayMessage {
	private static LanguageGenerator languageGenerator = new LanguageGenerator();
	private static Hashtable<String, LanguageEntry> syntaxMap;
	public DisplayMessage()
	{
		
	}
	
	public static void generateLanguage()
	{
		languageGenerator.generateLoadLanguage();
	}
	
	public static void setLanguage(String language)
	{
		System.out.println("language:"+language);
		languageGenerator.setLanguage(language);
		languageGenerator.updateLanguageMapLoadLanguage();
		syntaxMap = languageGenerator.getSyntaxMapLanguage();	
	}
	
	public static void printSuccessMessage(String successMessage)
	{
		String messageString = successMessage;
		if (syntaxMap.containsKey(messageString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageString);
			messageString = titleEntry.getLabel();
		}
		String messageTitleString = "Success";
		if (syntaxMap.containsKey(messageTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageTitleString);
			messageTitleString = titleEntry.getLabel();
		}
		String okString = "Ok";
		if (syntaxMap.containsKey(okString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(okString);
			okString = titleEntry.getLabel();
		}
		JOptionPane.showOptionDialog(null, messageString, messageTitleString, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{okString}, "default");
	}
	
	public static void printErrorMessage(String errorMessage)
	{
		String messageString = errorMessage;
		if (syntaxMap.containsKey(messageString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageString);
			messageString = titleEntry.getLabel();
		}
		String messageTitleString = "Error";
		if (syntaxMap.containsKey(messageTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageTitleString);
			messageTitleString = titleEntry.getLabel();
		}
		String okString = "Ok";
		if (syntaxMap.containsKey(okString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(okString);
			okString = titleEntry.getLabel();
		}
		JOptionPane.showOptionDialog(null, messageString, messageTitleString, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{okString}, "default");
	}
	public static void printErrorMessage(String errorMessage, String suffix)
	{
		String messageString = errorMessage;
		if (syntaxMap.containsKey(messageString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageString);
			messageString = titleEntry.getLabel();
		}
		String messageTitleString = "Error";
		if (syntaxMap.containsKey(messageTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageTitleString);
			messageTitleString = titleEntry.getLabel();
		}
		String okString = "Ok";
		if (syntaxMap.containsKey(okString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(okString);
			okString = titleEntry.getLabel();
		}
		JOptionPane.showOptionDialog(null, messageString+suffix, messageTitleString, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{okString}, "default");
	}
	public static void printMessage(String message)
	{
		String messageString = message;
		if (syntaxMap.containsKey(messageString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageString);
			messageString = titleEntry.getLabel();
		}
		String messageTitleString = "Message";
		if (syntaxMap.containsKey(messageTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(messageTitleString);
			messageTitleString = titleEntry.getLabel();
		}
		String okString = "Ok";
		if (syntaxMap.containsKey(okString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(okString);
			okString = titleEntry.getLabel();
		}
		JOptionPane.showOptionDialog(null, messageString, messageTitleString, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{okString}, "default");
	}
	public static int saveMessageUserInput(String saveMessage)
	{
		int response = 0;
		String saveConfirmationString = saveMessage;
		if (syntaxMap.containsKey(saveConfirmationString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveConfirmationString);
			saveConfirmationString = titleEntry.getLabel();
		}
		String saveString = "Save";
		if (syntaxMap.containsKey(saveString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveString);
			saveString = titleEntry.getLabel();
		}
		String yesString = "Yes";
		if (syntaxMap.containsKey(yesString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(yesString);
			yesString = titleEntry.getLabel();
		}
		String noString = "No";
		if (syntaxMap.containsKey(noString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(noString);
			noString = titleEntry.getLabel();
		}
		response = JOptionPane.showOptionDialog(null, 
				saveConfirmationString, 
				saveString+"?", 
		        JOptionPane.OK_CANCEL_OPTION, 
		        JOptionPane.INFORMATION_MESSAGE, 
		        null, 
		        new String[]{yesString, noString},
		        "default");
		return response;
	}
	
	public static String convertedTextInSelectedLanguage(String originalString)
	{
		String convertedString = originalString;
		if (syntaxMap.containsKey(originalString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(originalString);
			convertedString = titleEntry.getLabel();
		}
		return convertedString;		
	}
}
