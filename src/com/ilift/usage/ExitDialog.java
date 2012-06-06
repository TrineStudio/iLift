package com.ilift.usage;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class ExitDialog {

	public void ShowDialog(Context context) {
		System.out.println("退出");
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确定要退出吗");
		builder.setTitle("提醒");
		builder.setPositiveButton("退出", new Exit());
		builder.setNegativeButton("取消", new Cancel());
		builder.create().show();
	}

	class Cancel implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
		}
	}

	class Exit implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {

			System.exit(0);

		}

	}
}