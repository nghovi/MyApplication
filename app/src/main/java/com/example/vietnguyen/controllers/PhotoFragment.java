package com.example.vietnguyen.controllers;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.database.DBHelper;
import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.myapplication.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class PhotoFragment extends MyFragment{

	public static final int	MEDIA_TYPE_IMAGE					= 1;
	public static final int	MEDIA_TYPE_VIDEO					= 2;
	public static final int	CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE	= 1888;
	private ImageView		preview;
	private EditText		title;
	private EditText		description;
	private TextView		previewText;
	private Bitmap			photo;
	private DBHelper		dbHelper;

	/**
	 * This variable is the container that will host our cards
	 */
	private CardContainer	mCardContainer;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_photo, container, false);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void buildLayout(){
		super.buildLayout();
		mCardContainer = (CardContainer)getView().findViewById(R.id.layoutview);

		Resources r = getResources();

		SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(activity);
//		AccessToken accessToken = activity.getAccessToken();
//		GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//			@Override
//			public void onCompleted(JSONObject user, GraphResponse response) {
//				MU.log("klsdjfsjfsjf" + response.toString());
//                /* handle the result */
//			}
//		}).executeAsync();

		new GraphRequest(
				AccessToken.getCurrentAccessToken(),
				"/me/photos",
				null,
				HttpMethod.GET,
				new GraphRequest.Callback() {
					public void onCompleted(GraphResponse response) {
            /* handle the result */
						MU.log("klsdjfsjfsjf" + response.getJSONObject().toString());
					}
				}
		).executeAsync();

//		getApi(Const.FACEBOOK_GET_ALBUMS, MU.buildJsonObj(Arrays.asList("access_token", activity.getAccessToken())));

		if(android.os.Build.VERSION.SDK_INT >= 21){
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3, activity.getTheme())));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1, activity.getTheme())));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2, activity.getTheme())));
		}else{
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title6", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
			adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
			adapter.add(new CardModel("Title4", "Description goes here", r.getDrawable(R.drawable.picture1)));
			adapter.add(new CardModel("Title5", "Description goes here", r.getDrawable(R.drawable.picture2)));
		}

		CardModel cardModel = new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1));
		cardModel.setOnClickListener(new CardModel.OnClickListener() {

			@Override
			public void OnClickListener(){
				Log.i("Swipeable Cards", "I am pressing the card");
			}
		});

		cardModel.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {

			@Override
			public void onLike(){
				Log.i("Swipeable Cards", "I like the card");
			}

			@Override
			public void onDislike(){
				Log.i("Swipeable Cards", "I dislike the card");
			}
		});

		adapter.add(cardModel);

		mCardContainer.setAdapter(adapter);
	}

	private void gotoBookDetail(Book book){
		BookDetailFragment frg = new BookDetailFragment();
		frg.setBook(book);
		activity.addFragment(frg);
	}

	private void setOnClickPreview(View preview){
		preview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.take_video), getString(R.string.cancel)};
				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int option){
						switch(option){
						case 0: // take photo
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
							break;
						case 1:
							break;
						default:
							return;
						}
					}
				});
				builder.create().show();
			}
		});
	}

	private void setOnClickSave(Button btnSave){
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				String titleText = title.getText().toString();
				String descriptionText = description.getText().toString();
				if(photo != null && !titleText.equals("") && !descriptionText.equals("")){
					File sd = Environment.getExternalStorageDirectory();
					String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
					saveBitmapPhoto(photo, sd, fileName);
					// save image information to database, update form screen
					dbHelper.insertImage(titleText, descriptionText, sd + "/" + fileName);
					updateUI();
				}else{
					Log.e("######", "Todo: make a toast to remind user take a photo");
					Toast.makeText(getActivity(), getString(R.string.remind), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	public void saveBitmapPhoto(Bitmap bitmapPhoto, File sd, String fileName){
		File dest = new File(sd, fileName);
		try{
			FileOutputStream out = new FileOutputStream(dest);
			bitmapPhoto.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void updateUI(){
		photo = null;
		previewText.setVisibility(View.VISIBLE);
		preview.setImageDrawable(null);
		title.setText("");
		description.setText("");
	}

	/*
	 * After taking a photo
	 */
	// public void onActivityResult(int requestCode, int resultCode, Intent data) {
	// if (requestCode == PhotoFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	// if (resultCode == Activity.RESULT_OK) {
	// ImageView preview = (ImageView) getView().findViewById(R.id.imageView);
	// photo = (Bitmap) data.getExtras().get("data");
	// preview.setImageBitmap(photo);
	// } else if (resultCode == Activity.RESULT_CANCELED) {
	// Log.e("#######", "Capture image: result canceled");
	// } else {
	// Log.e("#######", "Capture image: result undefined");
	// }
	// }
	// }

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediafileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

		if(!mediaStorageDir.exists()){
			if(!mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if(type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		}else if(type == MEDIA_TYPE_VIDEO){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		}else{
			return null;
		}

		return mediaFile;
	}
}
