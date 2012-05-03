package com.Sitp.iLift;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class ExitDialog {

	public void ShowDialog(Context context) {
		System.out.println("�˳�");
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("ȷ��Ҫ�˳���");
		builder.setTitle("����");
		builder.setPositiveButton("�˳�", new Exit());
		builder.setNegativeButton("ȡ��", new Cancel());
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