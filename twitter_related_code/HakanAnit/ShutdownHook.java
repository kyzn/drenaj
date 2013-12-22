import java.io.IOException;

import org.apache.lucene.index.IndexWriter;


public class ShutdownHook extends Thread
{
	private IndexWriter writer;
	private TextAnalytics ta;
	public ShutdownHook(IndexWriter _writer,TextAnalytics ta)
	{
		writer=_writer;
		this.ta=ta;
	}
	public void run()
    {
        try
        {
        	//ta.storeWordIdsToFile();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
