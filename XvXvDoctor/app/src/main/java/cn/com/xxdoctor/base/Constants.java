package cn.com.xxdoctor.base;

import android.os.Environment;

/**
 * Created by chong.han on 2018/8/22.
 */

public interface Constants {

    String USER_ID = "UserID";
    String HOSPITAL_ID = "HospitalID";
    String LOGIN_NAME = "LoginName";
    String Name = "Name";
    String SEX = "Sex";
    String NATIONAL = "National";
    String BRITHDAY = "Brithday";
    String MOBILE = "Mobile";
    String ID_CARD = "IDCard";
    String PHOTO_ID = "PhotoID";
    String PHOTO_URL = "PhotoUrl";
    String ADDRESS = "Address";
    String FILEURL = "FileUrl";

    String MOBILE_TYPE = "1";
    String EVENTBUS_TYEPE = "type";
    String EVENTBUS_VALUE = "value";

    String tempPath = Environment.getExternalStorageDirectory() + "/" + "xxDoctor/image/";

    //    http://111.17.215.37:8023/skyapp_xy/api/app_patient/?
//    String IP = DoctorBaseAppliction.isTestState ? "http://192.168.0.250:8080/" :
//            "http://111.17.215.37:8023/";
    String IP = DoctorBaseAppliction.isTestState ? "http://111.17.215.37:8023/" :
            "http://111.17.215.37:8023/";
    String Host = IP + "skyapp_xy/api/app_patient/?";
    //用户登录
    String USER_LOGIN = Host + "act=login&data=";
    //患者列表
    String PATIENTS_LIST = Host + "act=patientslist&data=";
    //患者信息
    String GET_PATIENT_DETAILS = Host + "act=getPatientDetails&data=";
    //添加患者信息
    String INSERT_PATIENT = Host + "act=insertPatient&data=";
    //编辑患者信息
    String UPDATE_PATIENT = Host + "act=UpdatePatient&data=";
    //删除患者信息
    String DELETE_PATIENT = Host + "act=DeletePatient&data=";
    //查询标签列表
    String SELECT_DOCTOR_LABLE_LIST = Host + "act=selectDoctorLableList&data=";
    //医生添加标签
    String INSERT_DOCTOR_LABLE = Host + "act=insertDoctorLable&data=";
    //医生修改标签
    String UPDATE_DOCTOR_LABLE = Host + "act=updateDoctorLable&data=";
    //医生删除标签
    String DELETE_DOCTOR_LABLE = Host + "act=deleteDoctorLable&data=";
    //患者添加标签
    String INSERT_LABLE = Host + "act=insertLable&data=";
    //患者删除标签
    String DELETE_LABLE = Host + "act=deleteLable&data=";
    //查询患者随访记录
    String FOLLOW_INFO_RMATION_INDEX = Host + "act=FollowInformationIndex&data=";
    //添加患者随访记录
    String SAVE_FOLLOW_INFORMATION = Host + "act=saveFollowInformation&data=";
    //删除患者随访记录
    String DELETE_FOLLOW_RECORD = Host + "act=deleteFollowRecord&data=";
    //查询随访方式
    String FOLLOW_TYPE_LIST = Host + "act=FollowTypeList&data=";
    //终止随访记录
    String STOP_FOLLOW_RECORD = Host + "act=stopFollowRecord&data=";
    //随访方案列表
    String FOLLOW_LIST = Host + "act=followList&data=";
    //查询方案信息
    String INSERT_OR_UPDATE_FOLLOW = Host + "act=insertOrUpdateFollow&data=";
    //添加方案信息
    String INSERT_FOLLOW = Host + "act=insertFollow&data=";
    //修改方案信息
    String UPDATE_FOLLOW = Host + "act=updateFollow&data=";
    //辩证论治
    String SELECT_FILD_MODULE_LIST = Host + "act=selectFildModuleList&data=";
    //病症管理列表
    String SELECT_FIELD_RECORD_SIGN_LIST = Host + "act=selectFieldRecordSignList&data=";
    //查询辩证论治详情
    String SELECT_FIELD_CONTENTS_LIST = Host + "act=selectFieldContentsList&data=";
    //添加辩证论治数据
    String INSERT_OR_UPDATE_FIELD_CONTENTS = Host + "act=insertOrUpdateFieldContents&data=";
    //查询病程报告
    String SELECT_FIELD_RECORD_LIST_FORM = Host + "act=selectFieldRecordListForm&data=";
    //查询医生好友列表
    String SELECT_JMESSAGE_FRIENDS_LIST = Host + "act=selectJMessageFriendsList&data=";
    //添加好友
    String ADD_FRIENDS = Host + "act=addFriends&data=";
    //图片上传
    String UPLOAD_PIC = IP + "form/fileUpload.html?";
    //量表图片
    String SCALE_FILE_UPLOAD = IP + "form/scaleFileUpload.html?";
    //药方照片上传解析
    String FILE_UP_LOAD_APP_OCR = IP + "form/fileUploadAppOCR.html?";
    //查询图片列表
    String SELECT_FILE_SIGN_LIST_NEW = Host + "act=selectFilesignListNew&data=";
    //删除照片
    String DELETE_IMAGE = Host + "act=deleteImage&data=";
    //查询最后一次填写记录  添加复诊的时候自动调入首诊或者距离最近的复诊所填的内容，医生修改不同处即可。
    String select_Field_Record_Last_Details = Host + "act=selectFieldRecordLastDetails&data=";
    //患者随访列表
    String SELECT_FOLLOW_PATIENT_NEW_LIST = Host + "act=selectFollowPatientNewList&data=";
    //停诊
    String SUSPEND_MEDICAL = Host + "act=suspendMedical&data=";
    //获取我的量表列表
    String SELECT_SCALE_LIST = Host + "act=selectScaleList&data=";
    //修改量表共享状态
    String UPDATA_SCALE_IS_SHARED = Host + "act=updataScaleIsShared&data=";
    //查询课题列表
    String SELECT_SUBJECT_LIST = Host + "act=selectSubjectList&data=";
    //添加课题
    String INSERT_SUBJECT = Host + "act=insertSubject&data=";
    //修改课题
    String UPDATE_SUBJECT = Host + "act=updateSubject&data=";
    //查看详情
    String SELECT_SUBJECT_FIELD_LIST = Host + "act=selectSubjectfieldList&data=";
    //删除课题
    String DELETE_SUBJECT = Host + "act=deleteSubject&data=";
    //我的课题当中设置入选人群初始化条件
    String SET_MY_SUBJECT_OPTION = Host + "act=setMySubjectOption&data=";
    //我的课题当中设置入选人群初始化条件
    String GET_SUBJECT_OPTION_CONTENTS = Host + "act=getSubjectOptionContents&data=";
    //新随访列表
    String SELECT_FOLLOW_PLAN_LIST = Host + "act=selectFollowPlanList&data=";
    //数据字典
    String SELECT_CONTENT = Host + "act=selectContent&data=";
    //查询我的随访方案任务项目
    String SELECT_FOLLOW_CRF_LIST_NEW = Host + "act=selectFollowCRFListNew&data=";
    //查询我的随访方案任务项目
    String INSERT_FOLLOW_PLAN = Host + "act=insertFollowPlan&data=";
    //删除我的随访方案任务项目
    String DELETE_FOLLOW_PLAN = Host + "act=deleteFollowPlan&data=";
    //获取我的意见列表
    String SELECT_MY_OPINIONS_LIST = Host + "act=selectMyOpinionsList&data=";
    //添加意见
    String INSERT_MY_OPINION = Host + "act=insertMyOpinion&data=";
    //获取pdf下载链接
    String TO_PDF = Host + "act=toPdf&data=";
    //随访CRF详情查询
    String SELECT_FOLLOW_RECORD_CONTENTS_DETAILS = Host +
            "act=selectFollowRecordContentsDetails&data=";
    //删除病程
    String DELETE_COURSE_OF_DISEASE = Host + "act=deleteCourseOfDisease&data=";
    //修改首诊复诊
    String UPDATE_FRIST_OR_DOUBLE_DIAGNOSIS = Host + "act=updateFristOrDoubleDiagnosis&data=";
}
