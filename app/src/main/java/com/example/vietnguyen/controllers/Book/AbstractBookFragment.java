package com.example.vietnguyen.controllers.Book;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.vietnguyen.Const;
import com.example.vietnguyen.core.controller.DialogBuilder;
import com.example.vietnguyen.core.controller.MyFragment;
import com.example.vietnguyen.core.network.Api;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.models.Book;
import com.example.vietnguyen.models.MyModel;
import com.example.vietnguyen.myapplication.R;

import org.json.JSONObject;

public abstract class AbstractBookFragment extends MyFragment implements View.OnClickListener {

    public final static String KEY_UPDATED_BOOK = "book_updated";
    protected Book book;
    protected String newWord;

    @Override
    protected void buildLayout() {
        super.buildLayout();
        builFoldActionAndOnClickEvents();
        buildCoverImage();
        buildBookInfo();
        buildVocabulary();
    }

    abstract protected void buildBookInfo();

    abstract protected void buildVocabulary();

    protected void builFoldActionAndOnClickEvents() {
        setFoldAction(getView(R.id.lnr_sbe_icon_url_selectable), getImageView(R.id.img_sbe_icon_url_fold_icon), R.id.edt_sbe_icon_url, null);
        setFoldAction(getView(R.id.lnr_sbe_comment_selectable), getImageView(R.id.img_sbe_fold), R.id.edt_sbe_comment, null);
        setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null);
        setFoldAction(getView(R.id.lnr_sbe_author_selectable), getImageView(R.id.img_sbe_author_fold_icon), R.id.edt_sbe_author, null);
        setFoldAction(getView(R.id.lnr_sbe_mood_selectable), getImageView(R.id.img_sbe_mood_fold_icon), R.id.edt_sbe_mood, null);
        setFoldAction(getView(R.id.lnr_sbe_link_selectable), getImageView(R.id.img_sbe_link_fold_icon), R.id.edt_sbe_link, null);
        setFoldAction(getView(R.id.lnr_sbe_name_selectable), getImageView(R.id.img_sbe_name_fold_icon), R.id.edt_sbe_name, null);
        setOnClickFor(R.id.ico_sbe_add_vocabulary, this);
    }

    protected void buildCoverImage() {
        addTextWatcherForBookImage();
        MU.picassaLoadImage(book.iconUrl, getImageView(R.id.img_sbe_image), activity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_icon_done:
                savedBookFromLayout();
                break;
            case R.id.txt_fba_done:
                savedBookFromLayout();
                break;
            case R.id.ico_sbe_add_vocabulary:
                showDialogForAddingWord();
                break;
            default:
                break;
        }
    }

    protected void addTextWatcherForBookImage() {
        getEditText(R.id.edt_sbe_icon_url).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String url = editable.toString();
                MU.picassaLoadImage(url, getImageView(R.id.img_sbe_image), activity);
            }
        });
    }


    // /////////////////////////////////////////////////////////////////////////////////////////////////

    protected void showDialogForAddingWord() {
        dlgBuilder.buildDialogWith2Edt(activity, "Enter new word", "Enter new phrase", new DialogBuilder.OnDialogWithEdtDismiss() {

            @Override
            public void onClickDone(String input1, String input2) {
                addWordForBook(input1, input2);
            }
        }).show();
    }

    abstract protected void addWordForBook(String word, String phrase);

    protected void addPhraseForExistingWord(List<Book> booksContainWord, String newWord, String newPhrase) {
        Book b = booksContainWord.get(0);
        if (!MU.isEmpty(newPhrase)) {
            b.addPhraseForWord(newWord, newPhrase);
            savedBookFromLayout();
            showExistedWordNotifyDialog("Added new phrase for '" + newWord + "' at '" + b.name + "'", newWord, b);
        } else {
            showExistedWordNotifyDialog("Found '" + newWord + "' at '" + b.name + "'", newWord, b);
        }
    }

    private void showExistedWordNotifyDialog(String msg, String newWord, final Book foundBook) {
        dlgBuilder.build2OptionsDialog(activity, "Word existed!", msg, "View", new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                activity.addFragment(new BookDetailFragment(), AbstractBookFragment.KEY_UPDATED_BOOK, foundBook);
            }
        }).show();
    }

    protected void showDialogConfirmDeleteWord(final String word) {
        dlgBuilder.buildConfirmDlgTopDown("Cancel", "Delete", new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                book.deleteWord(word);
                savedBookFromLayout();
                buildVocabulary();
            }
        }).show();
    }

    protected void showDialogForAddingPhrase(final String word) {
        dlgBuilder.buildDialogWithEdt(activity, "Enter new phrase for " + word, null, new DialogBuilder.OnDialogWithEdtDismiss() {

            @Override
            public void onClickDone(String input1, String input2) {
                addPhraseForWord(word, input1);
                buildVocabulary();
            }
        }).show();
    }

    protected void addPhraseForWord(String word, String phrase) {
        if (book.hasWord(word) && !MU.isEmpty(phrase)) {
            book.addPhraseForWord(word, phrase);
            savedBookFromLayout();
        }
    }

    protected void deletePhrase(String word, String phrase) {
        book.deletePhraseForWord(word, phrase);
        savedBookFromLayout();
    }

    protected void savedBookFromLayout() {
        book.name = getEditText(R.id.edt_sbe_name).getText().toString();
        book.comment = getEditText(R.id.edt_sbe_comment).getText().toString();
        book.author = getEditText(R.id.edt_sbe_author).getText().toString();
        book.mood = getEditText(R.id.edt_sbe_mood).getText().toString();
        book.iconUrl = getEditText(R.id.edt_sbe_icon_url).getText().toString();
        book.link = getEditText(R.id.edt_sbe_link).getText().toString();
        book.save();
    }

    protected boolean hasChangeData() {
        return !book.author.equals(getEditText(R.id.edt_sbe_author).getText().toString()) || !book.name.equals(getEditText(R.id.edt_sbe_name).getText().toString()) || !book.link.equals(getEditText(R.id.edt_sbe_link).getText().toString()) || !book.comment.equals(getEditText(R.id.edt_sbe_comment).getText().toString()) || !book.iconUrl.equals(getEditText(R.id.edt_sbe_icon_url).getText().toString()) || !book.mood.equals(getEditText(R.id.edt_sbe_mood).getText().toString());
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public static List<MyModel> searchWithConditions(Map<String, Object> conditions) {
        String word = (String) conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_WORD);
        String name = (String) conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_NAME);
        String author = (String) conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_AUTHOR);
        String comment = (String) conditions.get(BookSearchFragment.KEY_BOOK_SEARCH_COMMENT);

        List<MyModel> books = Book.getAllUndeleted(Book.class);
        Iterator<MyModel> ib = books.iterator();
        while (ib.hasNext()) {
            Book book = (Book) ib.next();
            if (!MU.isEmpty(word) && !book.hasWord(word)) {
                ib.remove();
                continue;
            }
            if (!MU.isEmpty(name) && !MU.checkMatch(book.name, name)) {
                ib.remove();
                continue;
            }
            if (!MU.isEmpty(author) && !MU.checkMatch(book.author, author)) {
                ib.remove();
                continue;
            }
            if (!MU.isEmpty(comment) && !MU.checkMatch(book.comment, comment)) {
                ib.remove();
                continue;
            }
        }
        return books;
    }
}
