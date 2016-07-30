package Xposed;

import java.util.Map;

import com.hdj.downapp_market.activity.SetClickUtil;
import com.mz.utils.EasyOperateClickUtil;
import com.mz.utils.GoogelAccountUtil;
import com.mz.utils.MyFileUtil;
import com.mz.utils.GlobalConstants;
import com.mz.utils.Logger;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import Xposed.JobAbstract;
import Xposed.MethodInt;

public class JobGP extends JobAbstract {
	boolean removed=false;
	boolean nocrarChoose=false;
	private View continueBtn = null;
	private static JobGP instance;
	private View installBtn = null;
	private View acceptBtn = null;
	private int cannotInstallTimes = 0;
	private JobGP() {
	}
	public static JobGP getGP(){
		if(instance==null){
			synchronized (JobGP.class) {
				if(instance==null){
					instance=new JobGP();
				}
			}
		}
		return instance;
	}
	@Override
	public void handleMethod(String packageName, ClassLoader classLoader) {
		gpInstall_click=false;
		gpAccountAdd=false;
		nocrarChoose=false;
		cannotInstallTimes=0;
		removed=false;
		continueBtn =null;
		acceptBtn =null;
		//AppsPermissionsActivity
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.GP_DOWN);
		
		HookMethod(View.class, "getMeasuredWidth", packageName,
				MethodInt.GP_TV_PROGRESS);
		HookMethod(TextView.class, "setText", packageName,
				MethodInt.GP_TV_PROGRESS,CharSequence.class);
		
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.GP_TV_INSTALL);
		HookMethod(TextView.class, "isSuggestionsEnabled", packageName,
				MethodInt.GP_TV_INSTALL);

		HookMethod(View.class, "dispatchTouchEvent", packageName,
				MethodInt.DIS, MotionEvent.class);
		HookMethod("com.google.android.finsky.activities.AppsPermissionsActivity", "onCreate", classLoader,packageName, MethodInt.GP_ACCOUNT_DIALOG,Bundle.class);
		
		HookMethod(ImageView.class, "setImageDrawable",packageName, MethodInt.GP_RATE_TV,Drawable.class);
		HookMethod(View.class, "findViewById",packageName, MethodInt.GP_RATE_TV,int.class);
		
		
		HookMethod(View.class, "refreshDrawableState", packageName,
				MethodInt.GP_RATE);
//		HookMethod("com.google.android.finsky.activities.MainActivity", "onCreate", classLoader,packageName, MethodInt.GP_RATE_TV,Bundle.class);

		
		
	}
	//�����Ϣ
	/**
	 * ���ܣ�2131820862  ;��װ2131820959  ;����2��2131821105   ;12%   2131820974   ;ж��2131820953
	 * 
	 *  GOOGLE����================����16908313  128 true  public void android.view.View.refreshDrawableState()
	 */
	long installFirst,installSecond;
	@Override
	public void handleAftreMethod(String packageName,MethodHookParam param, int type) {
		// TODO Auto-generated method stub
		super.handleAftreMethod(packageName,param, type);
		Object obj;
		switch (type) {
		case MethodInt.GP_DOWN://-16908313=====ȷ��
			obj=param.thisObject;
//			if (obj instanceof android.support.v7.widget.RecyclerView)
			if( obj instanceof TextView){//-2131820959=====��װ
				TextView text = (TextView) obj;
				if("Ϊ��Ӧ������ ".equals(text.getText().toString())){
					Logger.i("#########-------Ϊ��Ӧ������-----"+text.getId()+"  "+param.method);
				}
				if((text.getId()==2131820950||text.getId()==2131820959)&&text.getText()!=null&&"��װ".equals(text.getText().toString())){
					Logger.i("GOOGLE��װ================"+text.getText()+text.getId()+"::visibility::"+text.getVisibility());
					if(text.getVisibility()==View.VISIBLE){
						installSecond=System.currentTimeMillis();
//						if(installSecond-installFirst>=1000){
							installBtn=text;
							boolean res=SetClickUtil.setClick(text);
//							boolean res2=SetClickUtil.setSimulateClick(text, 0, 0);
							Logger.i("GOOGLE��װ================"+text.getText()+text.getId()+"  "+text.getWidth()+" "+res+param.method);
							Message msg = handler.obtainMessage();
							msg.obj=text;
							msg.what=MethodInt.GP_DOWN;
							handler.sendMessageDelayed(msg, 10*1000);
							installFirst=installSecond;
//						}
						
					}
				}//2131820862=====����=  
				if((text.getId()==2131820858||text.getId()==2131820862)&&text.getText()!=null&&"����".equals(text.getText().toString())&&text.getWidth()>0){
					boolean res=SetClickUtil.setClick(text);
					Logger.i("GOOGLE����================"+text.getText()+text.getId()+"  "+text.getWidth()+" "+res+"  "+param.method);
				}
				if(text.getId()==16908313&&text.getText()!=null&&"����".equals(text.getText().toString())&&text.getWidth()>0){
					boolean res=SetClickUtil.setClick(text);
					Logger.i("GOOGLE����================"+text.getText()+text.getId()+"  "+text.getWidth()+" "+res+"  "+param.method);
				}
				if(text.getId()==16908313&&text.getText()!=null&&"ȷ��".equals(text.getText().toString())&&text.getWidth()>0){
					boolean res=SetClickUtil.setClick(text);
					boolean res2=SetClickUtil.setClick(installBtn);
					cannotInstallTimes++;
					Logger.i("GOOGLE=�޷�����==ȷ��============="+text.getText()+text.getId()+"  "+" "+res+" res2: "+res2+"  cannotInstallTimes: "+cannotInstallTimes+"  "+param.method);
					if(cannotInstallTimes>=10){
						SetClickUtil.recodeUselessAccount(text.getContext());
					}
				}
				//-2131821105=====����
				if((text.getId()==2131821093||text.getId()==2131821105)&&text.getText()!=null&&"����".equals(text.getText().toString())/*&&text.getWidth()>0*/){
					boolean res=SetClickUtil.setClick(text);
					acceptBtn=text;
					Logger.i("GOOGLE����222222================"+text.getText()+text.getId()+"  "+"  "+res+"  "+param.method);
				}
				//����˻�ʱ
				if(text.getId()==2131820728&&"������".equals(text.getText().toString())){
					ViewGroup parent=(ViewGroup) text.getParent();
					View view1=parent.getChildAt(0);
					if( view1 instanceof RadioButton){
						RadioButton checkBox = (RadioButton) view1;
						Logger.i("�����˲����˲����˲�checkBox========"+checkBox.getText()+"   "+checkBox.isChecked());
						if(checkBox.isChecked()){
							nocrarChoose=true;
							if(continueBtn!=null){
								boolean res=SetClickUtil.setClick(continueBtn);
								Logger.i("�����˲����˲�����=====continueBtn========"+continueBtn+"   "+res);
							}
						}
					}
					if(!nocrarChoose){
						boolean res=SetClickUtil.setSimulateClick(parent, 0, 0);
						Logger.i("�����˲����˲����˲����˲����˲�����========="+text.getText()+res);
					}
				}
				if(text.getId()==2131820862&&"����".equals(text.getText().toString())){
					continueBtn = text;
					Logger.i("����������������=============="+text.getText()+text.getId()+nocrarChoose+"   continuebtn---"+continueBtn);
					
					if(nocrarChoose){
						boolean res=SetClickUtil.setClick(text);
						Logger.i("����������������=============="+text.getText()+text.getId()+"  "+text.getWidth()+"  "+res+"  "+param.method);
						if(res) nocrarChoose=false;
					}
				}
				if("���ӳ�ʱ".equals(text.getText().toString())||"����������".equals(text.getText().toString())){
					Logger.i("GOOGLE���ӳ�ʱ============="+text.getText());
					if(EasyOperateClickUtil.getAutoClickFlag(text.getContext())==EasyOperateClickUtil.AUTOCLICK){
						SetClickUtil.recodeUselessAccount(text.getContext());
					}
				}
				if(EasyOperateClickUtil.getIsAutoInjectAccountFlag(text.getContext())==EasyOperateClickUtil.AUTO_INJECT_ACCOUNT){
					AccountManager accountManager = AccountManager.get(text.getContext());
					Account[] accounts = accountManager.getAccountsByType("com.google");
					for (int i = 0; i < accounts.length; i++) {
						Account account=accounts[i];
					}
					if(accounts.length<=0) EasyOperateClickUtil.setIsAddAccountFlag(text.getContext(), EasyOperateClickUtil.ADD_ACCOUNT);
					else {
						EasyOperateClickUtil.setIsAddAccountFlag(text.getContext(), EasyOperateClickUtil.DO_NOT_ADD_ACCOUNT);
					}
					if(EasyOperateClickUtil.getIsAddAccountFlag(text.getContext())==EasyOperateClickUtil.DO_NOT_ADD_ACCOUNT) return;
					
					Map<String,String> map=MyFileUtil.getAccount();
					Logger.i("======ACCOUNT======map=============="+map);
					if(map!=null){
						String email=map.get(GlobalConstants.MAP_EMAIL);
//						String password=map.get(GlobalConstants.MAP_PASS);
//						Logger.i("=======ACCOUNT=======text-----------email-------"+email+"======password==="+password);
						String oldPassword=map.get(GlobalConstants.MAP_PASS);
						String password=GoogelAccountUtil.getPassword(text.getContext(), email,oldPassword);
						Logger.i("=======ACCOUNT=======text-----------email-------"+email+"======password==="+oldPassword+"\n  pass:"+password);
						Account acc = new Account(email, "com.google");
						Bundle bundle=new Bundle();
						bundle.putInt("flag", 1);
						bundle.putString("services", "hist,mail");
						Logger.i("=====ACCOUNT=============text**==bundle=="+bundle);
						if(accountManager.addAccountExplicitly(acc, password, bundle)) ;
						Logger.i("======ACCOUNT==========text====================acc================="+acc.name+ "  "+acc.type+"   "+ "  pass22:"+accountManager.getPassword(acc)+":");
						EasyOperateClickUtil.setIsAddAccountFlag(text.getContext(), EasyOperateClickUtil.DO_NOT_ADD_ACCOUNT);	
					}
				}
				
			}
			break;
		case MethodInt.GP_TV_PROGRESS:
			obj=param.thisObject;
		
			if( obj instanceof TextView){
				TextView text = (TextView) obj; //���� 
				int halfValue = EasyOperateClickUtil.getHalfDownloadValue(text.getContext());
				if (halfValue == 100)
					return;
				if((text.getId()==2131820960||text.getId()==2131820974)&&text.getText()!=null){
					Logger.i("======"+text.getText()+"  "+param.method);
					String proStr=text.getText().toString();
					if(proStr.contains("%")){
						int index = proStr.indexOf("%");
						float progress =Float.parseFloat(proStr.substring(0, index)) ;
						Logger.i("======progress======="+progress);
						if(progress>=halfValue){
							SetClickUtil.recodeUsedAccount(text.getContext());
						}
					}
				}
				
			}
			break;
		case MethodInt.GP_TV_INSTALL:
			obj=param.thisObject;
			
			if( obj instanceof TextView){
				TextView text = (TextView) obj; //2131821757
				if(text.getId()==2131821722||text.getId()==2131821757/*&&!TextUtils.isEmpty(text.getText())&&"����".equals(text.getText().toString())*/){
					boolean res=SetClickUtil.setClick(text);
					Logger.i("GOOGLE����================"+text.getText()+text.getId()+"  "+text.getWidth()+"  "+res+"  "+param.method);
				}
				if((text.getId()==2131820944||text.getId()==2131820953)&&text.getText()!=null&&"ж��".equals(text.getText().toString())&&text.getVisibility()==View.VISIBLE){
					Logger.i("GOOGLE ж��============="+text.getText()+text.getId()+text.getWidth()+"  "+text.getVisibility()+"  "+param.method);
					SetClickUtil.recodeUsedAccount(text.getContext());
				}
			}
			if(obj instanceof CheckBox){
				CheckBox checkBox = (CheckBox)obj;
				if("��ʹ��WLAN����".equals(checkBox.getText().toString())){
					if(checkBox.isChecked()){
						checkBox.performClick();
					}
				}
			}
			break;
		case MethodInt.GP_ACCOUNT_DIALOG:
			obj=param.thisObject;
//			Logger.i("---------GP_ACCOUNT_DIALOG------------"+obj.getClass().toString()+"--------"+param.method);
			if(obj!=null&&obj.getClass().toString().equals("class com.google.android.finsky.activities.AppsPermissionsActivity")){
				Activity a=(Activity)obj;
				Button v=(Button) a.findViewById(2131821093);
				if(v==null){
					v=(Button) a.findViewById(2131821105);
				}if(v!=null){
//					v.refreshDrawableState();
					boolean res=SetClickUtil.setSimulateClick(v, 0, 0);
//					v.refreshDrawableState();
					Logger.i("---------GP_ACCOUNT_DIALOG-----------v-"+v.getClass()+"-------"+"   "+v.getId()+"  ��"+v.getText().toString()+"��  "+v.isClickable()+" res: "+res);
				}
				if(EasyOperateClickUtil.getAutoClickFlag(v.getContext())==EasyOperateClickUtil.AUTOCLICK){
						Message msg = handler.obtainMessage();
						msg.obj=a;
						msg.what=MethodInt.GP_ACCOUNT_DIALOG;
						handler.sendMessageDelayed(msg, 10*1000);
				}
			}
			break;
		case MethodInt.GP_RATE_TV:
			obj = param.thisObject;
			Object[] args = param.args;
////			Logger.i("###################===PlayRatingStar---------image.getId()------------"+obj.getClass().toString()+"==="+(obj instanceof ImageView)+ param.method);
//			if(obj instanceof ImageView){
//				ImageView image = (ImageView) obj;
//				if(image.getId()==2131821791){
//					Logger.i("###################==PlayRatingStar---------image.getId()------------"+2131821791+image.isClickable());
//				}
//				if(args.length==1&& (args[0] instanceof Integer)){
//					int id = ((Integer)args[0]).intValue();
//					if(id==2131821791){
//						if(param.getResult()!=null){
//							Logger.i("---------------------##########PlayRatingStar---------------------"+args[0]+ obj.getClass().toString()+param.method);
//							Object res=param.getResult();
//							if(res instanceof View){
//								View vi=(View) res;
//								ViewGroup group=(ViewGroup) vi.getParent();
//								if(group!=null){
//									Logger.i("-----��������--group-----##########PlayRatingStar---------------------"/*+group.findViewById(2131821791)*/);
//								}
//								boolean cl=SetClickUtil.setClick(vi);
//								Logger.i("-----��������--View-----##########PlayRatingStar---------------------"+cl+"   vi.getParent():"+ vi.getParent());
//
//							}
//						}
//					}
//				}
//			}
			if(obj.getClass().toString().equals("class com.google.android.finsky.layout.play.PlayRatingStar")){
				Logger.i("###################==PlayRatingStar---------------------"+(obj instanceof ImageView)+"--method-"+param.method);
				ImageView image = (ImageView) obj;
				if(image.getId()==2131821791){
					Logger.i("###################==PlayRatingStar---------image.getId()------------"+2131821791+image.isClickable());
					View view=(View) image.getParent();
					if(view!=null){
						Logger.i("###################=$$$$$$$$$$$$$$=####PlayRatingStar---------------------"+view.getClass()+(view instanceof ViewGroup));
					}
				}
			}
			
//				if(id==2131820950||id==2131820959){
////					Logger.i("-----------��װ----------##########PlayRatingStar---------------------"+id+ obj.getClass().toString()+param.method);
//					Object res=param.getResult();
//					if(res instanceof View){
//						View vi=(View) res;
//						Context context=vi.getContext();
////						vi.getWindowId()
//						ViewGroup group=null;
//						while (vi.getParent()!=null) {
//							group=(ViewGroup) vi.getParent();
//							View rat=group.findViewById(2131821791);
//							if(rat!=null)
//								{
//								Logger.i("-----rat!=null---����������װ--rat-----------##########P---------------"+group+"--rat---"+rat);
//								break;
//								}
//						}
////						((ViewGroup)context.findViewById(android.R.id.content)).getChildAt(0);
////						Logger.i("--------����������װ--View-----------##########PlayRatingStar---------------------"+id+ obj.getClass().toString()+"-----"+param.method);
//					}
//				}
//			}
//			if(obj instanceof Activity){
//				Activity ac=(Activity)obj;
//				View view = ((ViewGroup)ac.findViewById(android.R.id.content)).getChildAt(0);
//				Logger.i("####################################################################"+view+"-----------"+view.findViewById(2131820959));
//				if(view instanceof ViewGroup){
//					ViewGroup viewGroup = (ViewGroup) view;
//					Logger.i("#######################################viewGroup#############################"+viewGroup.getChildCount()+"---"+viewGroup.getChildAt(1));
//					if(viewGroup.getChildCount()>=2){
//						View secondView=viewGroup.getChildAt(1);
//						if(secondView instanceof ViewGroup){
//							ViewGroup secondViewGroup=(ViewGroup)secondView;
//							Logger.i("###################################secondViewGroup############################"+secondViewGroup.getChildCount()+"---"+viewGroup.getChildAt(0));
//						}
//					}
//				}
//			}
			break;
		case MethodInt.GP_RATE:
			obj=param.thisObject;
//			Logger.i("+++++++++++GP_RATE----------------------------------"+obj.getClass().toString());

			if (obj.getClass().toString().equals("class android.support.v7.widget.RecyclerView")){
				if(obj instanceof View){
					View view = (View) obj;
					Logger.i("+++++++++++GP_RATE---------------------2131821791----------------------------"+view.findViewById(2131821791));
				}
				Logger.i("+++++++++++++++GP_RATE-------------------------------------------------");
			}
			break;
		default:
			break;
		}
	}

}
