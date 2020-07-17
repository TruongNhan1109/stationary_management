package com.ttn.stationarymanagement.presentation.dialog_fragment;

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

import com.ttn.stationarymanagement.R;
import com.ttn.stationarymanagement.data.local.WorkWithDb;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.utils.CustomToast;
import com.ttn.stationarymanagement.utils.GetDataToCommunicate;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddDepartmentDialog extends DialogFragment {

    public static final String TAG = AddDepartmentDialog.class.getSimpleName();

    @BindView(R.id.tv_dialog_add_department_close)
    TextView tvClose;

    @BindView(R.id.edt_dialog_add_department_name)
    EditText edtNameDepartment;

    @BindView(R.id.edt_dialog_add_department_note)
    EditText edtNote;

    @BindView(R.id.btn_dialog_add_department_add)
    Button btnAdd;

    private CompositeDisposable compositeDisposable;

    public interface AddDepartmentDilaogListener {
        public void onAddSuccesstion();
        public void onUpload(String department, String note);
    }

    private boolean isUpload = false;

    private AddDepartmentDilaogListener mListener;

    public void setListener(AddDepartmentDilaogListener listener) {
        this.mListener = listener;
    }

    public static AddDepartmentDialog newInstance(String department, String note) {
        Bundle args = new Bundle();
        args.putString("NAME", department);
        args.putString("NOTE",note );
        AddDepartmentDialog fragment = new AddDepartmentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_department, container, false);
        ButterKnife.bind(this, view);
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

    // Lấy dữ liệu phòng ban cần cập nhật
    private void getData() {

        String name = getArguments().getString("NAME", "");
        String note = getArguments().getString("NOTE", "");

        if (!TextUtils.isEmpty(name)) {     // Có thông tin cần cập nhật

            // Đưa dữ liệu cần được lưu lên hiển thị
            edtNameDepartment.setText(name);
            edtNote.setText(note);
            btnAdd.setText("Cập nhật");
            isUpload = true;

        }

    }

    private void setEvents() {

        // Khi click vào nút hủy
        tvClose.setOnClickListener(v -> dismiss());

        // Khi nhấn vào nút thêm
        btnAdd.setOnClickListener(v -> {

            // Kiểm tra tên phòng ban
            if (TextUtils.isEmpty(edtNameDepartment.getText().toString())) {
                edtNameDepartment.setError("Phòng ban không được để trống");
                edtNameDepartment.requestFocus();
                return;
            }

            if (isUpload) {     // Cập nhật phòng ban
                if (mListener != null) {
                    String name = edtNameDepartment.getText().toString();
                    String note = !TextUtils.isEmpty(edtNote.getText().toString()) ? edtNote.getText().toString() : "";
                    mListener.onUpload(edtNameDepartment.getText().toString(), note);
                    dismiss();
                }
            } else {        // Tạo mới phòng ban
                createDepartment();
            }

        });

    }

    private void createDepartment() {

        // Ob tạo phòng ban
        Observable<Boolean> obCreateDepartment = Observable.create(r -> {

            try {

                // Set các giá trị phòng ban cần tạo
                PhongBan phongBan = new PhongBan();
                phongBan.setTenPB(edtNameDepartment.getText().toString());
                phongBan.setGhiChu(!TextUtils.isEmpty(edtNameDepartment.getText().toString()) ? edtNote.getText().toString() : "");
                phongBan.setNgayTao(GetDataToCommunicate.getCurrentDate()); // Ngày tạo

                r.onNext( WorkWithDb.getInstance().insert(phongBan)); // Lưu xuống Db
                r.onComplete();

            } catch (Exception e) {
                e.printStackTrace();
                r.onError(e);
            }

        });

        compositeDisposable.add(obCreateDepartment.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {

            if (aBoolean) {
                CustomToast.showToastSuccesstion(getContext(), "Thêm thành công", Toast.LENGTH_SHORT);

                if (mListener != null) {
                    mListener.onAddSuccesstion();
                    dismiss();
                }

            } else {
                CustomToast.showToastError(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT);
            }

        }, throwable -> {
            CustomToast.showToastError(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT);
        }));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}
