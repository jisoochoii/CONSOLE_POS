package services.backend.project;

import database.project.DataAccess;
/*************김재필***************/
public class MemberManagements {//회원등록업무만

	public MemberManagements() {
		
	}
	
	//회원등록 요청
	private String regMemberCtl(String data) {
		String message = "회원등록 실패";
		String[] memberInfo = {"회원코드","회원이름"};
		String[] memberValue;
		DataAccess dao = new DataAccess();
		memberValue = this.rmCtls(memberInfo,data);
		
		//매장코드 유무 확인
		if(this.isMemberCode(memberValue[0], dao)) {
			System.out.println("동일한 회원코드 존재.");
				message = this.insMember(memberValue, dao)?
						"요청하신 회원등록이 완료되었습니다." : "회원등록이 실패하였습니다.";
			}
		
		return message;
	}
	
	//회원등록
	private boolean insMember(String[] goodsInfo, DataAccess dao) { 
		boolean isCheck = false;
		StringBuffer sb = new StringBuffer();
		if(dao.fileConnection(false,"E:\\Smartweb\\BYW\\java_project\\src\\database\\project\\Member.txt",true)) {			
			for(int idx=0; idx<goodsInfo.length; idx++) {
				sb.append(goodsInfo[idx]);
				if(idx<goodsInfo.length-1) {
					sb.append(",");
				}
			}
			isCheck = dao.ins(sb.toString());
		}
		dao.fileClose();
		return isCheck;
	}
	
	//입력받은값을 배열화시키고 db데이터와 비교
	private String[] rmCtls(String[] itemInfo, String data) {
		String[] item = data.split("\\&");
		String[] itemValue = new String[item.length];
		
		for(int idx=0;idx<item.length; idx++) {
			for(int idx2=0; idx2<itemInfo.length; idx2++) {
				if(item[idx].contains(itemInfo[idx2])) {
					itemValue[idx] = item[idx].substring(itemInfo[idx2].length()+1);
					break;
				}
			}
		}
		return itemValue;
	}
	
	//회원코드 유효성검사
	private boolean isMemberCode(String memberCode, DataAccess dao)  {
		boolean isCheck = false;
		String[ ] mCodeList = null;
		
		if(dao.fileConnection(true,"E:\\Smartweb\\BYW\\java_project\\src\\database\\project\\Member.txt",false)) {			
			mCodeList = dao.isStoreCode().split(",");
			for(int idx=0; idx<mCodeList.length; idx++) {
				isCheck = true;
				break;
			}
		}
		dao.fileClose();
		return isCheck;
	}
	

}
