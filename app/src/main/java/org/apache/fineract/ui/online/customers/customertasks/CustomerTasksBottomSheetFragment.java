package org.apache.fineract.ui.online.customers.customertasks;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.fineract.R;
import org.apache.fineract.data.models.customer.Command;
import org.apache.fineract.data.models.customer.Customer;
import org.apache.fineract.ui.base.FineractBaseActivity;
import org.apache.fineract.ui.base.FineractBaseBottomSheetDialogFragment;
import org.apache.fineract.ui.base.Toaster;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Rajan Maurya
 *         On 27/07/17.
 */
public class CustomerTasksBottomSheetFragment extends FineractBaseBottomSheetDialogFragment
        implements CustomerTasksBottomSheetContract.View {

    @BindView(R.id.iv_task1)
    ImageView ivTask1;

    @BindView(R.id.iv_task2)
    ImageView ivTask2;

    @BindView(R.id.tv_task1)
    TextView tvTask1;

    @BindView(R.id.tv_task2)
    TextView tvTask2;

    @BindView(R.id.ll_task2)
    LinearLayout llTask2;

    @BindView(R.id.ll_task_list)
    LinearLayout llTaskList;

    @BindView(R.id.ll_task_form)
    LinearLayout llTaskForm;

    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.tv_sub_header)
    TextView tvSubHeader;

    @BindView(R.id.et_comment)
    EditText etComment;

    @BindView(R.id.btn_submit_task)
    Button btnSubmitTask;

    @Inject
    CustomerTasksBottomSheetPresenter tasksBottomSheetPresenter;

    View rootView;

    private BottomSheetBehavior behavior;
    private Customer.State state;
    private Command command;
    private String customerIdentifier;
    private OnTasksChangeListener onTasksChangeListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        rootView = View.inflate(getContext(), R.layout.bottom_sheet_task_list, null);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) rootView.getParent());
        ((FineractBaseActivity) getActivity()).getActivityComponent().inject(this);
        tasksBottomSheetPresenter.attachView(this);
        ButterKnife.bind(this, rootView);
        command = new Command();

        switch (state) {
            case ACTIVE:
                ivTask1.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_lock_black_24dp));
                tvTask1.setText(getString(R.string.lock));
                ivTask2.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_black_24dp));
                tvTask2.setText(getString(R.string.close));
                break;
            case PENDING:
                llTask2.setVisibility(View.GONE);
                ivTask1.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_check_circle_black_24dp));
                ivTask1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.status));
                tvTask1.setText(getString(R.string.activate));
                break;
            case LOCKED:
                ivTask1.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_lock_open_black_24dp));
                ivTask1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.status));
                tvTask1.setText(getString(R.string.un_lock));
                ivTask2.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_close_black_24dp));
                tvTask2.setText(getString(R.string.close));
                break;
            case CLOSED:
                llTask2.setVisibility(View.GONE);
                ivTask1.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                        R.drawable.ic_check_circle_black_24dp));
                ivTask1.setColorFilter(ContextCompat.getColor(getActivity(), R.color.status));
                tvTask1.setText(getString(R.string.reopen));
                break;
        }

        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && isTaskFormVisible()) {
                hideTaskForm();
                return true;
            }
            return false;
        });

        return dialog;
    }

    @OnClick(R.id.iv_task1)
    void onClickTask1() {
        switch (state) {
            case ACTIVE:
                command.setAction(Command.Action.LOCK);
                tvHeader.setText(getString(R.string.lock));
                tvSubHeader.setText(
                        getString(R.string.please_verify_following_task, getString(R.string.lock)));
                btnSubmitTask.setText(getString(R.string.lock));
                state = Customer.State.LOCKED;
                break;
            case PENDING:
                command.setAction(Command.Action.ACTIVATE);
                tvHeader.setText(getString(R.string.activate));
                tvSubHeader.setText(getString(R.string.please_verify_following_task,
                        getString(R.string.activate)));
                btnSubmitTask.setText(getString(R.string.activate));
                state = Customer.State.ACTIVE;
                break;
            case LOCKED:
                command.setAction(Command.Action.UNLOCK);
                tvHeader.setText(getString(R.string.un_lock));
                tvSubHeader.setText(getString(R.string.please_verify_following_task,
                        getString(R.string.un_lock)));
                btnSubmitTask.setText(getString(R.string.un_lock));
                state = Customer.State.ACTIVE;
                break;
            case CLOSED:
                command.setAction(Command.Action.REOPEN);
                tvHeader.setText(getString(R.string.reopen));
                tvSubHeader.setText(getString(R.string.please_verify_following_task,
                        getString(R.string.reopen)));
                btnSubmitTask.setText(getString(R.string.reopen));
                state = Customer.State.ACTIVE;
                break;
        }
        llTaskList.setVisibility(View.GONE);
        llTaskForm.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_task2)
    void onClickTask2() {
        command.setAction(Command.Action.CLOSE);
        tvHeader.setText(getString(R.string.close));
        tvSubHeader.setText(getString(R.string.please_verify_following_task,
                getString(R.string.close)));
        btnSubmitTask.setText(getString(R.string.close));
        state = Customer.State.CLOSED;
        llTaskList.setVisibility(View.GONE);
        llTaskForm.setVisibility(View.VISIBLE);
    }

    public void setCustomerStatus(Customer.State customerStatus) {
        state = customerStatus;
    }

    public void setCustomerIdentifier(String identifier) {
        customerIdentifier = identifier;
    }

    @OnClick(R.id.btn_submit_task)
    void submitTask() {
        command.setComment(etComment.getText().toString().trim());
        etComment.setEnabled(false);
        tasksBottomSheetPresenter.changeCustomerStatus(customerIdentifier, command);
    }

    @OnClick(R.id.btn_cancel)
    void onCancel() {
        hideTaskForm();
    }

    public void setCustomerTasksChangeListener(OnTasksChangeListener onTasksChangeListener) {
        this.onTasksChangeListener = onTasksChangeListener;
    }

    @Override
    public void statusChangedSuccessfully() {
        Toast.makeText(getActivity(), R.string.task_updated_successfully, Toast.LENGTH_LONG).show();
        onTasksChangeListener.changeCustomerStatus(state);
        hideTaskForm();
    }

    @Override
    public void showProgressbar() {
        showMifosProgressDialog(getString(R.string.updating_status));
    }

    @Override
    public void hideProgressbar() {
        hideMifosProgressDialog();
    }

    @Override
    public void showNoInternetConnection() {
        etComment.setEnabled(true);
        Toaster.show(rootView, getString(R.string.no_internet_connection));
    }

    @Override
    public void showError(String message) {
        etComment.setEnabled(true);
        Toaster.show(rootView, getString(R.string.error_updating_status));
    }

    @Override
    public void onStart() {
        super.onStart();
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideMifosProgressDialog();
        tasksBottomSheetPresenter.detachView();
    }

    private boolean isTaskFormVisible() {
        return llTaskForm.getVisibility() == View.VISIBLE
                && llTaskList.getVisibility() == View.GONE;
    }
    private void hideTaskForm() {
        llTaskForm.setVisibility(View.GONE);
        llTaskList.setVisibility(View.VISIBLE);
        etComment.getText().clear();
    }
}
