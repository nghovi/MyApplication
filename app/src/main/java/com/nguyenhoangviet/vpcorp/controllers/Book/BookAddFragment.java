package com.nguyenhoangviet.vpcorp.controllers.Book;

import java.util.List;

import com.nguyenhoangviet.vpcorp.core.utils.MU;
import com.nguyenhoangviet.vpcorp.models.Book;
import com.nguyenhoangviet.vpcorp.myapplication.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BookAddFragment extends AbstractBookFragment{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		book = new Book();
		book.iconUrl = getString(R.string.fragment_abstract_book_default_link);
		book.link = getString(R.string.fragment_abstract_book_default_icon_url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_book_add, container, false);
	}

	@Override
	protected void buildLayout(){
		super.buildLayout();
		getMainActivity().footer.hide();
	}

	@Override
	protected int getRightImageId(){
		return R.drawable.ic_done_white_36dp;
	}

	@Override
	protected String getHeaderTitle(){
		return getString(R.string.fragment_book_add_title);
	}

	@Override
	public void onRightImgClicked(){
		onDoneClicked();
	}

	@Override
	protected void buildBookInfo(){
		setTextFor(R.id.edt_sbe_link, book.link);
		setTextFor(R.id.edt_sbe_icon_url, book.iconUrl);
	}

	@Override
	protected void buildVocabulary(){
		LayoutInflater inflater = (LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout lnrVocabulary = getLinearLayout(R.id.lnr_sbe_vocabulary_list);
		lnrVocabulary.removeAllViews();
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		for(final String word : this.book.getWords()){
			View itemBookWordEdit = inflater.inflate(R.layout.item_book_word_edit, null);

			setTextFor(itemBookWordEdit, R.id.txt_ibwe_word, word);
			setOnClickFor(itemBookWordEdit, R.id.lnr_ibwe_add_phrase, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showDialogForAddingPhrase(word);
				}
			});
			setOnClickFor(itemBookWordEdit, R.id.img_ibwe_delete, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					showDialogConfirmDeleteWord(word);
				}
			});
			builPhrasesForWord(word, itemBookWordEdit, inflater);
			lnrVocabulary.addView(itemBookWordEdit);
		}
	}

	private void builPhrasesForWord(final String word, View itemBookWord, LayoutInflater inflater){
		List<String> phrases = this.book.getWordUsage(word);
		setFoldAction(getView(itemBookWord, R.id.lnr_ibwe), getImageView(itemBookWord, R.id.img_ibwe_fold), R.id.lnr_ibwe_foldable, getView(itemBookWord, R.id.img_ibwe_delete), null);
		if(word.equals(this.newWord)){
			getTextView(itemBookWord, R.id.txt_ibwe_word).setTextColor(getResources().getColor(R.color.core_blue));
			getView(itemBookWord, R.id.lnr_ibwe).performClick();
		}
		LinearLayout lnrPhrases = getLinearLayout(itemBookWord, R.id.lnr_ibwe_phrases);
		lnrPhrases.removeAllViews();
		for(final String phrase : phrases){
			View line = inflater.inflate(R.layout.item_phrase_edit, null);
			setTextFor(line, R.id.txt_ipe_phrase, phrase);
			setOnClickFor(line, R.id.lnr_item_phrase_edit, new View.OnClickListener() {

				@Override
				public void onClick(View view){
					dlgBuilder.buildConfirmDlgTopDown(getString(R.string.cancel), getString(R.string.delete), new View.OnClickListener() {

						@Override
						public void onClick(View view){
							deletePhrase(word, phrase);
						}
					}).show();
				}
			});
			lnrPhrases.addView(line);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onClickBackBtn(){
		if(!hasChangeData()){
			activity.backToFragment(BookListFragment.class);
		}else{
			dlgBuilder.buildConfirmDlgTopDown(getString(R.string.continue_str), getString(R.string.discard), new View.OnClickListener() {

				@Override
				public void onClick(View view){
					activity.backOneFragment();
				}
			}).show();
		}
	}

	@Override
	protected void addWordForBook(String newWord, String newPhrase){
		if(!MU.isEmpty(newWord) && !book.hasWord(newWord)){
			this.newWord = newWord;
			book.addWordForBook(this.newWord);
			if(!MU.isEmpty(newPhrase)){
				book.addPhraseForWord(newWord, newPhrase);
			}
			buildVocabulary();
		}
	}

	// public void addBookToServer(){
	// if(book.isReadyToSave()){
	// book.save();
	// showShortToast("Successfully saved new book");
	// activity.backOneFragment();
	// JSONObject params = MU.buildJsonObj(Arrays.<String>asList("book", book.toString()));
	// postApi(Const.ADD_BOOK, params, new Api.OnCallApiListener() {
	//
	// @Override
	// public void onApiResponse(JSONObject response){
	// showShortToast("Successfully saved new book");
	// book.id = response.optString("data");
	// book.isRemoteSaved = true;
	// book.save();
	// activity.backOneFragment();
	// }
	//
	// @Override
	// public void onApiError(String errorMsg){
	// book.save();
	// }
	// });
	// }else{
	// showLongToast("Please fullfill information");
	// }
	//
	// }
}
