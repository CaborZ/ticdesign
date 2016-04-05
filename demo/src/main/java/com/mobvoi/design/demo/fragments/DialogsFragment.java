package com.mobvoi.design.demo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.mobvoi.design.demo.data.Cheeses;
import com.ticwear.design.demo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ticwear.design.app.AlertDialog;
import ticwear.design.app.DatetimePickerDialog;
import ticwear.design.app.NumberPickerDialog;

/**
 * Created by tankery on 1/12/16.
 *
 * fragment for dialogs
 */
public class DialogsFragment extends ListFragment {

    static final String TAG = "TicDialogs";

    private CharSequence[] valuePickers;
    private CharSequence[] listChoices;

    @Override
    protected int[] getItemTitles() {
        return new int[]{
//                R.string.category_dialog_notify,
                R.string.category_dialog_confirm,
                R.string.category_dialog_choose,
                R.string.category_dialog_value_picker,
                R.string.category_dialog_choice,
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valuePickers = new CharSequence[] {
                getResources().getString(R.string.category_dialog_number_picker),
                getResources().getString(R.string.category_dialog_time_picker),
                getResources().getString(R.string.category_dialog_date_picker),
                getResources().getString(R.string.category_dialog_datetime_picker),
        };
        listChoices = new CharSequence[] {
                getResources().getString(R.string.category_dialog_single_selection),
                getResources().getString(R.string.category_dialog_single_choice),
                getResources().getString(R.string.category_dialog_multiple_choice),
        };
    }

    @Override
    public void onTitleClicked(View view, @StringRes int titleResId) {
        Dialog dialog = createDialog(view.getContext(), titleResId);
        if (dialog != null) {
            dialog.show();
        }
    }

    private Dialog createValuePickerDialog(Context context, int which) {
        Dialog dialog = null;
        switch (which) {
            case 0:
                dialog = new NumberPickerDialog.Builder(context)
                        .minValue(0)
                        .maxValue(20)
                        .defaultValue(5)
                        .valuePickedlistener(new NumberPickerDialog.OnValuePickedListener() {
                            @Override
                            public void onValuePicked(NumberPickerDialog dialog, int value) {
                                Toast.makeText(dialog.getContext(), "Picked value " + value,
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .create();
                break;
            case 1: {
                dialog = new DatetimePickerDialog.Builder(context)
                        .defaultValue(Calendar.getInstance())
                        .disableDatePicker()
                        .listener(new DatetimePickerDialog.OnCalendarSetListener() {
                            @Override
                            public void onCalendarSet(DatetimePickerDialog dialog, Calendar calendar) {
                                Toast.makeText(dialog.getContext(), "Picked time: " +
                                                SimpleDateFormat.getTimeInstance().format(calendar.getTime()),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                        .create();
                break;
            }
            case 2: {
                dialog = new DatetimePickerDialog.Builder(context)
                        .defaultValue(Calendar.getInstance())
                        .disableTimePicker()
                        .listener(new DatetimePickerDialog.OnCalendarSetListener() {
                            @Override
                            public void onCalendarSet(DatetimePickerDialog dialog, Calendar calendar) {
                                Toast.makeText(dialog.getContext(), "Picked date: " +
                                                SimpleDateFormat.getDateInstance().format(calendar.getTime()),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                        .create();
                break;
            }
            case 3: {
                dialog = new DatetimePickerDialog.Builder(context)
                        .defaultValue(Calendar.getInstance())
                        .listener(new DatetimePickerDialog.OnCalendarSetListener() {
                            @Override
                            public void onCalendarSet(DatetimePickerDialog dialog, Calendar calendar) {
                                Toast.makeText(dialog.getContext(), "Picked datetime: " +
                                                SimpleDateFormat.getDateTimeInstance().format(calendar.getTime()),
                                        Toast.LENGTH_LONG)
                                        .show();
                            }
                        })
                        .create();
                break;
            }
        }

        return dialog;
    }

    private Dialog createListChoiceDialog(Context context, int which) {
        Dialog dialog = null;
        final String[] listItems = Cheeses.getRandomCheesesList();
        switch (which) {
            case 0:
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.category_dialog_single_selection)
                        .setItems(listItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Picked: " + listItems[which],
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .create();
                break;
            case 1: {
                class SelectionHolder {
                    public int which;
                }
                final SelectionHolder selectionHolder = new SelectionHolder();
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.category_dialog_single_choice)
                        .setSingleChoiceItems(listItems, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectionHolder.which = which;
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Toast.makeText(getActivity(), "Picked: " + listItems[selectionHolder.which],
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .create();
                break;
            }
            case 2: {
                final List<Integer> selection = new ArrayList<>();
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.category_dialog_multiple_choice)
                        .setMultiChoiceItems(listItems, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    selection.add(which);
                                } else {
                                    selection.remove((Integer) which);
                                }
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                String message = "Picked item:\n";
                                for (int which : selection) {
                                    message += listItems[which] + ";\n";
                                }
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                break;
            }
        }

        return dialog;
    }

    private Dialog createDialog(final Context context, int resId) {
        Dialog dialog = null;
        switch (resId) {
            case R.string.category_dialog_notify:
                break;
            case R.string.category_dialog_confirm:
                dialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.category_dialog_confirm)
                        .setMessage(R.string.cheese_content)
                        .setPositiveButtonIcon(R.drawable.ic_btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                break;
            case R.string.category_dialog_choose:
                dialog = new AlertDialog.Builder(context)
                        .setTitle(R.string.category_dialog_choose)
                        .setMessage(R.string.cheese_content)
                        .setPositiveButtonIcon(R.drawable.ic_btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButtonIcon(R.drawable.ic_about, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                break;
            case R.string.category_dialog_value_picker: {
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.category_dialog_value_picker)
                        .setItems(valuePickers, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog dlg = createValuePickerDialog(getActivity(), which);
                                if (dlg != null) {
                                    dlg.show();
                                }
                            }
                        })
                        .create();
                break;
            }
            case R.string.category_dialog_choice: {
                dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.category_dialog_choice)
                        .setItems(listChoices, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dialog dlg = createListChoiceDialog(getActivity(), which);
                                if (dlg != null) {
                                    dlg.show();
                                }
                            }
                        })
                        .create();
                break;
            }
        }

        return dialog;
    }

}