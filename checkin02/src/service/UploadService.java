package service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import util.UploadUtil;
import util.UploadUtil.OnUploadProcessListener;
import util.UploadUtil.uploadProcessListener;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;

public class UploadService extends Service implements OnUploadProcessListener{
	private Binder localBinder = new localBinder();
	public final static int TO_UPLOAD_FILE = 0;
	public final static String requestURL = "http://192.168.1.104:8080/MyTest/p/file!upload";
	private String picpath;
	private int status;

	public UploadService() {
		// TODO �Զ����ɵĹ��캯�����
	}

	public class localBinder extends Binder {
		public UploadService getService() {
			// �����ﷵ�ص�ǰService������Activity�е��ô˷����󼴿ɵõ�Service������
			return UploadService.this;
		}

		@Override
		// data�Ǵ�������ݣ�reply�Ƿ��ص�����
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			// TODO �Զ����ɵķ������
			status = data.readInt();
			picpath = data.readString();
			System.out.println("--->>status" + status);
			System.out.println("--->>picpath" + picpath);
			if (status == TO_UPLOAD_FILE) {
				System.out.println("---->>ok");
				toUploadFile();
			}
			reply.writeInt(13);
			reply.writeString("tom");
			return super.onTransact(code, data, reply, flags);
		}
	}

	private void toUploadFile() {
		UploadUtil uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this);
		String fileKey = "img";
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderId", "111");
		params.put("name", "456");
		System.out.println("--->>picpath" + picpath + "--->>fileKey" + fileKey
				+ "--->>requestURL" + requestURL);
		uploadUtil.uploadFile(picpath, fileKey, requestURL, params);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO �Զ����ɵķ������
		return localBinder;
	}

	@Override
	public void onUploadDone(int responseCode, String message) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void initUpload(int fileSize) {
		// TODO �Զ����ɵķ������
		
	}
}
