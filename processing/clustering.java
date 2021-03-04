package processing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class clustering {
	public static void main (String[] args) throws SQLException{
		MyUtils.Init();
		PreparedStatement stmt =MyUtils.conn.prepareStatement("select article.title, article.pmid from medline13.article, medline13.journal, zotero.subject" +
				" where article.pmid= journal.pmid and journal.issn=subject.issn and subject.subject = 'CARDIAC & CARDIOVASCULAR SYSTEMS' " +
				" and pub_year>1989 and pub_year<2011 ");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()){
			StringBuffer abst=new StringBuffer();
			
			String title = rs.getString(1);
			int pmid = rs.getInt(2);
			PreparedStatement stmt2 = MyUtils.conn.prepareStatement("select abstract_text " +
					"from medline13.abstr" +
					" where pmid=? order by seqnum desc");
			stmt2.setInt(1, pmid);
			ResultSet rs2 = stmt2.executeQuery();
			while (rs2.next()){
				abst.append(" "+rs2.getString(1));
			}
		}
	}

}
