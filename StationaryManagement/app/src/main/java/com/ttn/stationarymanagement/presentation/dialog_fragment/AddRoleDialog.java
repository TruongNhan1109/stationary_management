package com.ttn.stationarymanagement.presentation.dialog_fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.github.ybq.android.spinkit.style.Circle;
import com.github.ybq.android.spinkit.style.Wave;
import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRoleDialog extends DialogFragment {

    public static final String TAG = AddRoleDialog.class.getSimpleName();

    @BindView(R.id.btn_dialog_add_role_add)
    Button btnAdd;

    @BindView(R.id.edt_dialog_add_role_role)
    EditText edtRole;

    @BindView(R.id.tv_dialog_add_role_close)
    TextView tvClose;


    private CompositeDisposable compositeDisposable;
    private boolean isUpload = false;

    public interface AddRoleDialogListener {
        public void onAddSuccesstion();
        public void onUploadSuccesstionn(String change);
    }

    private AddRoleDialogListener mListener;

    private  String role = "";

    public static AddRoleDialog newInstance(String role) {
        Bundle args = new Bundle();
        args.putString("ROLE", role);

        AddRoleDialog fragment = new AddRoleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(AddRoleDialogListener listener) {
        this.mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_role, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        compositeDisposable = new CompositeDisposable();
        getData();
        setEvents();

    }

    private void getData() {
        String nameRole = getArguments().getString("ROLE", "");

        if (!TextUtils.isEmpty(nameRole)) {
            edtRole.setText(nameRole);
            isUpload = true;
            btnAdd.setText("Cập nhật");
        }

    }

    private void setEvents() {
        btnAdd.setOnClickListener(v -> {

            if (TextUtils.isEmpty(edtRole.getText().toString())) {
                edtRole.setError("Không được để trống!");
                edtRole.requestFocus();
                return;
            }

            if (isUpload) {
                if (mListener != null) {
                    mListener.onUploadSuccesstionn(edtRole.getText().toString());
                    dismiss();
                }
            } else {
                createRole();
            }

        });

        tvClose.setOnClickListener(v -> dismiss());
    }

    private void createRole() {

     compositeDisposable.add( obCreateRole(edtRole.getText().toString()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                    if (aBoolean) {

                        CustomToast.showToastSuccesstion(getContext(), "Thêm thành công", Toast.LENGTH_SHORT);

                        if (mListener != null) {
                            mListener.onAddSuccesstion();
                            dismiss();
                        }

                    } else {
                        CustomToast.showToast(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT);
                    }
        }, throwable -> {
            CustomToast.showToastError(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT);
        }));

    }

    private Observable<Boolean> obCreateRole(String role) {
        return  Observable.create(r -> {
            try {
                VaiTro newRole = new VaiTro(edtRole.getText().toString());
                newRole.setNgayTao(GetDataToCommunicate.getCurrentDate());
                  r.onNext( WorkWithDb.getInstance().insert(newRole));
                  r.onComplete();

            } catch (Exception e) {
                e.printStackTrace();
                r.onError(e);
            }

        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        compositeDisposable.dispose();
    }
}
