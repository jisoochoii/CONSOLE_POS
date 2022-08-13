package services.backend.project;
/*************임현***************/
import database.project.DataAccess;

public class StoreManagements {
	//직원등록,직원비밀번호수정 매장수정여기서하기

	public String backController(String clientData) {
		String value = null;
		String[] client = clientData.split("\\?"); 

		if(client[0].equals("ST")) {
			value = this.regStoreCtl(client[1]); //매장명 수정
		}
		else if(client[0].equals("EI")) {
			value = this.regMemberCtl(client[1]); //직원 등록
		}
		else if(client[0].equals("EU")) {
			value = this.modMemberCtl(client[1]); //직원 비밀번호 수정
		}
		else if(client[0].equals("SA")) { //로그인
			value = this.loginCtl(client[1]);
		}
		return value;
	}
	
	//매장수정
		private String regStoreCtl(String data) {
			
			return null;
		}
		
	//직원등록
	private String regMemberCtl(String data) {
		//매장코드,직원코드,비밀번호,직원이름
		String message = "\n등록 실패";
		String[] employeesInfo = {"매장코드", "직원코드", "비밀번호", "직원이름"};
		String[] employeesValue;
		DataAccess dao = new DataAccess();
		String filePath = "E:\\smartweb\\java_1\\java_project\\src\\database\\project\\Employees.txt";
		employeesValue = this.rmCtls(employeesInfo,data);
		return message;
	}

	//등록(DAO)
	private boolean insDao(String filePath,String[] memberInfo, DataAccess dao) { 
		boolean isCheck = false;
		StringBuffer sb = new StringBuffer();
		if(dao.fileConnection(false, filePath, true)) {
			for(int idx=0; idx<memberInfo.length; idx++) {
				sb.append(memberInfo[idx]);
				if(idx<memberInfo.length-1){
					sb.append(",");
				}
			}
			isCheck = dao.ins(sb.toString());
		}
		dao.fileClose();
		return isCheck;
	}

	//직원비밀번호수정
	private String modMemberCtl(String data) {

		return null;
	}

	//입력받은값을 배열화시키고 db데이터와 비교
	private String[] rmCtls(String[] memberInfo, String data) {
		String[] item = data.split("\\&");
		String[] itemValue = new String[item.length];

		for(int idx=0;idx<item.length; idx++) {
			for(int idx2=0; idx2<memberInfo.length; idx2++) {
				if(item[idx].contains(memberInfo[idx2])) {
					itemValue[idx] = item[idx].substring(memberInfo[idx2].length()+1);
					break;
				}
			}
		} 
		return itemValue;
	}

	//매장코드 유무
	private boolean isStoreCode(String storeCode, DataAccess dao) {
		boolean isCheck = false;
		String[] mbCodeList = null;
		if(dao.fileConnection(true, "E:\\smartweb\\java_1\\java_project\\src\\database\\project\\Store.txt", false)) {
			mbCodeList = dao.isStoreCode().split(",");
			for(int idx=0; idx<mbCodeList.length; idx++) {
				if(mbCodeList[idx].equals(storeCode)) {
					isCheck = true;
					break;
				}
			}
		}
		dao.fileClose();
		return isCheck;
	}

	//직원코드 유무
	private boolean isMemberCode(String filePath, String[] memberValue, DataAccess dao) {
		boolean isCheck = false;
		String[] infoList = null;
		if(dao.fileConnection(true,filePath , false)) {
			infoList =  dao.isGoodsCode().split(",");
			for(int idx=0; idx<infoList.length; idx+=2) {	  
				if(infoList[idx].equals(memberValue[0])) {		 
					if(infoList[idx+1].equals(memberValue[1])) { 
						isCheck = true;
						break;
					}
				}
			}
		}
		dao.fileClose();
		return isCheck;
	}

	//비밀번호 유무
	private boolean isPassword(String filePath, String[] memberValue, DataAccess dao) {
		boolean isCheck = false;
		String[] infoList = null;
		
		if(dao.fileConnection(true,filePath , false)) {
			infoList =  dao.isGoodsCode().split(",");
			
			for(int idx=0; idx<infoList.length; idx+=3) {
				
				if(infoList[idx].equals(memberValue[0])) {	
					
					if(infoList[idx+1].equals(memberValue[1])) {
						
						if(infoList[idx+2].equals(memberValue[2])) { 
							
							isCheck = true;
							break;
						}
					}
				}
			}
		}
		dao.fileClose();
		return isCheck;
	}


	//일차원 배열로부터 특정 항목의 인덱스 파악
	private int getMemberIndex(String data, int memberIdx, String[] memberInfo) {
		int targetIdx = -1;
		String store = data.split("&")[memberIdx];
		String storeName = store.substring(0, store.indexOf("="));
		
		for(int idx=0; idx<memberInfo.length; idx++) {
			if(memberInfo[idx].equals(storeName)) {
				targetIdx = idx;
				break;
			}
		}
		return targetIdx;
	}
	
	//특정 테이블의 전체 레코드 수 파악
	private int getRecordCnt(String filePath, DataAccess dao) {
		int recordCnt = -1;
		if(dao.fileConnection(true, filePath, false)) {
			recordCnt = dao.getRecordCount();
		}
		dao.fileClose();
		return recordCnt;
	}
	
	//특정 테이블의 모든 데이터 수집
	private String[][] getDataInfo(String filePath, int recordSize, DataAccess dao) {
		String[][] dataInfo = null;
		
		if(dao.fileConnection(true, filePath, false)) {
			dataInfo = dao.getDataInfo(recordSize);
		}
		
		dao.fileClose();
		return dataInfo;
	}
	
	//기존 데이터를 최신데이터로 갱신 
	private void modValue(String[][] dataList, String[]memberValue, int memberIndex) {
		for(int recordIdx=0; recordIdx<dataList.length; recordIdx++) {
			if(dataList[recordIdx][0].equals(memberValue[0]) && dataList[recordIdx][1].equals(memberValue[1])){ 
				dataList[recordIdx][memberIndex] = memberValue[memberValue.length-1];
				break;
			}
		}
	}

	//수정(DAO)
	private boolean updTable(String filePath, String[][] dataList, DataAccess dao) {
		boolean isCheck = false;
		if(dao.fileConnection(false, filePath, false)) {
			isCheck = dao.upd(dataList);
		}
		dao.fileClose();
		return isCheck;
	}
	
	private String loginCtl(String data) {
		String[] itemInfo= {"매장코드","직원코드","비밀번호"};
		String[] itemValue;
		String message = "\n로그인 실패\n";
		DataAccess dao = new DataAccess();
		String filePath =  "E:\\smartweb\\java_1\\java_project\\src\\database\\project\\Employees.txt";
		itemValue = this.rmCtls(itemInfo,data);
		
		
		if(this.isStoreCode(itemValue[0],dao)) {
			System.out.print("\n매장코드 확인 완료");
		
			if(this.isMemberCode(filePath, itemValue,dao)) {
				System.out.print("\n직원코드 확인 완료");
				System.out.print(data);
				if(this.isPassword(filePath, itemValue,dao)) {
					System.out.print("\n비밀번호 확인 완료");
					
					message = "\n로그인 성공\n";
				}
			}
		}
		return message;
	}
}
