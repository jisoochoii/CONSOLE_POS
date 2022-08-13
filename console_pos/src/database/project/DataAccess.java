package database.project;

import java.util.ArrayList;
import java.io.*;
import beans.*;
 
/*************김재필***************/

public class DataAccess {
	File file;
	FileReader reader;
	FileWriter writer;
	BufferedReader buffer;
	BufferedWriter bWriter;
	public DataAccess() {}

	//읽기/쓰기
	public boolean fileConnection(boolean type , String f,boolean writeType) { 		
		boolean result;
		this.file = new File(f);
		try {
			if(type) {
				this.reader = new FileReader(this.file);
				this.buffer = new BufferedReader(this.reader);
			}else {
				if(!writeType) {
					this.writer = new FileWriter(this.file);
				}else {
					this.writer = new FileWriter(this.file, true);
				}
				this.bWriter = new BufferedWriter(this.writer);
			}
			result = true;
		} catch (FileNotFoundException e) {
			result = false;
		} catch(IOException e1) {
			result = false;
		}
		return false;
	}

	//닫아주기
	public void fileClose() {
		try {
			if(this.reader != null)	this.reader.close();
			//if(this.writer != null) this.writer.close();
		}
		catch (IOException e) {}
		finally {
			try {
				if(this.buffer != null) this.buffer.close();
				//if(this.bWriter != null) this.bWriter.close();
			} catch (IOException e) {}
		}
	}

	//매장코드 일치 확인
	public String isStoreCode() {
		StringBuffer result = new StringBuffer();
		String record = null;
		int idx = 0;

		try {
			while((record = this.buffer.readLine())!= null) {
				idx++;
				result.append((idx>1)?",": "");	
				result.append(record.substring(0, record.indexOf(",")));		
			}
		} catch (IOException e) {}
		return result.toString();
	}

	//상품코드유무
	public String isGoodsCode()  {
		StringBuffer result = new StringBuffer();
		String record = null;
		int idx = 0;
		try {
			while((record = this.buffer.readLine())!= null) {
				idx++;
				result.append((idx>1)?",": "");	
				result.append(record.substring(0, record.indexOf(",", record.indexOf(",")+1)));		
			}
		} catch (IOException e) {}
		return result.toString();
	}

	//수정
	public boolean upd(String[][] goodsInfo) { 
		boolean isCheck;
		StringBuffer sb = new StringBuffer();
		try {
			for(int recordIdx=0; recordIdx<goodsInfo.length; recordIdx++) {
				for(int columnIdx=0;columnIdx<goodsInfo[recordIdx].length; columnIdx++) {
					sb.append(goodsInfo[recordIdx][columnIdx]);
					sb.append((columnIdx != goodsInfo[recordIdx].length-1)?	",": "\n");
				}				
			}
			this.bWriter.write(sb.toString());
			this.bWriter.close();
			isCheck = true;
		} catch (IOException e) {
			isCheck = false;
		}
		return isCheck;
	}


	//등록
	public boolean ins(String goodsInfo) {
		boolean isCheck;
		try {
			this.bWriter.write(goodsInfo);
			this.bWriter.newLine();
			this.bWriter.close();
			isCheck = true;
		} catch (IOException e) {
			isCheck = false;
		}
		return isCheck;
	}

	//읽어오기
	public String[][] getDataInfo(int recordCounts){
		String[][] dataInfo = new String[recordCounts][];
		//goodsInfo로 해야하나?
		String record = null;
		int idx = -1;
		
		try {
			while((record = this.buffer.readLine())!= null) {
				idx++;
				dataInfo[idx] = record.split(",");		
			}
		} catch (IOException e) {}
		return dataInfo;
	}

	//db파일의 레코드갯수 확인
	public int getRecordCount() { 
		int count = 0;
		try {
			while(this.buffer.readLine()!= null) {
				count++;		
			}
		} catch (IOException e) {}
		return count;
	}

	//매장이름 찾기
	public String getStoreName(String storeCode) {
		String storeName = null, line = null ;
		String[] record = null;

		try {
			while((line = this.buffer.readLine()) != null){
				record = line.split(",");
				if(record[0].equals(storeCode)) {
					storeName = record[1];
					break;
				}
			}
		}catch(IOException e) {}
		return storeName;
	}

	//매장코드와 상품코드가 일치할때 상품정보불러오기
	public String[] salesGoodsInfo(String[] code) {
		String[] goodsInfo = null;
		String[] record =  null;
		String line = null;

		try {
			while((line = this.buffer.readLine()) != null){
				record = line.split(",");
				if(record[0].equals(code[0]) && record[1].equals(code[1])) {
					goodsInfo = new String[5];
					goodsInfo[0] = record[2];
					goodsInfo[1] = record[4];
					goodsInfo[2] = record[6];
					goodsInfo[3] = record[7];
					goodsInfo[4] = record[8];
					break;
				}
			}
		}catch(IOException e) {}
		return goodsInfo;
	}

	//결제정보 입력
	public boolean setOrder(ArrayList<OrderInfo> orderList) {
		boolean check = false;

		for(OrderInfo oi: orderList) {
			try {
				this.bWriter.write(oi.getOrderCode() + "," + oi.getGoodsCode() + "," + oi.getQuantity());
				this.bWriter.newLine();
				check = true;
			} catch (IOException e) {} finally{
				try {
					this.bWriter.close();
				} catch (IOException e) {}
			}
		}
		return check; 
	}
}
