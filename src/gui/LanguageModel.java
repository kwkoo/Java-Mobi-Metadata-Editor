package gui;

public interface LanguageModel
{
	public int getLocale();

	public int getDictInput();

	public int getDictOutput();

	public void setLanguages(int locale, int dictInput, int dictOutput);
}
