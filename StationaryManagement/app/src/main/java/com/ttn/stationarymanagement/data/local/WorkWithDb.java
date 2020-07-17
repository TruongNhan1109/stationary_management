package com.ttn.stationarymanagement.data.local;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.ttn.stationarymanagement.data.local.interactor.AllocationUseCase;
import com.ttn.stationarymanagement.data.local.interactor.DepartmentUseCase;
import com.ttn.stationarymanagement.data.local.interactor.ProductUseCase;
import com.ttn.stationarymanagement.data.local.interactor.RoleUseCase;
import com.ttn.stationarymanagement.data.local.interactor.StaftUseCase;
import com.ttn.stationarymanagement.data.local.model.CapPhat;
import com.ttn.stationarymanagement.data.local.model.NhanVien;
import com.ttn.stationarymanagement.data.local.model.PhongBan;
import com.ttn.stationarymanagement.data.local.model.VaiTro;
import com.ttn.stationarymanagement.data.local.model.VanPhongPham;
import com.ttn.stationarymanagement.presentation.baseview.MyApp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkWithDb {

    private static DatabaseHelper databaseHelper = null;
    private static final String TAG = WorkWithDb.class.getSimpleName();
    private static WorkWithDb workWithDb = null;
    private Context mContext;

    public static WorkWithDb getInstance(){
        if(workWithDb == null){
            workWithDb = new WorkWithDb();
        }
        return workWithDb;
    }

    public WorkWithDb(){
        mContext = MyApp.getAppContext();
        databaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
    }

    // VaiTro Table --------------------------------------------------------------------------------

    // Thêm vai trò
    public boolean insert(VaiTro role) {
        try {
           return  RoleUseCase.create(databaseHelper.getRoleDao(), role) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    // Cập nhật vai trò
    public boolean update(VaiTro role) {
        try {
            RoleUseCase.update(databaseHelper.getRoleDao(), role);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa vai trò
    public boolean delete(VaiTro role) {
        try {
            RoleUseCase.delete(databaseHelper.getRoleDao(), role);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public VaiTro getRoleById(long id) {
        VaiTro role = new VaiTro();
        try {
            role = RoleUseCase.getById(databaseHelper.getRoleDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;

    }

    // Lấy toàn bộ các vai trò
    public List<VaiTro> getAllRole() {
        try {
            List<VaiTro> list = RoleUseCase.getAll(databaseHelper.getRoleDao());
            if (list == null) {
                return new ArrayList<>();
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Department Table -----------------------------------------------------

    // Thêm phòng ban
    public boolean insert(PhongBan phongBan) {
        try {
            return  DepartmentUseCase.create(databaseHelper.getDepartmentDao(), phongBan) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }


    // Cập nhật phòng ban
    public boolean update(PhongBan phongBan) {
        try {
            DepartmentUseCase.update(databaseHelper.getDepartmentDao(), phongBan);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa phòng ban
    public boolean delete(PhongBan phongBan) {
        try {
            DepartmentUseCase.delete(databaseHelper.getDepartmentDao(), phongBan);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public PhongBan getDepartmentById(long id) {

        PhongBan phongBan = new PhongBan();
        try {
            phongBan = DepartmentUseCase.getById(databaseHelper.getDepartmentDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phongBan;

    }

    // Lấy danh sách phòng ban
    public List<PhongBan> getAllDepartment() {
        try {
            List<PhongBan> list = DepartmentUseCase.getAll(databaseHelper.getDepartmentDao());

            if (list == null) {
                return new ArrayList<>();
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Van phòng phẩm table ---------------------------------------

    public boolean insert(VanPhongPham vanPhongPham) {
        try {
            return  ProductUseCase.create(databaseHelper.getProductDao(), vanPhongPham) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean update(VanPhongPham vanPhongPham) {
        try {
            ProductUseCase.update(databaseHelper.getProductDao(), vanPhongPham);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(VanPhongPham vanPhongPham) {
        try {
            ProductUseCase.delete(databaseHelper.getProductDao(), vanPhongPham);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public VanPhongPham getProductById(long id) {
        try {
            return ProductUseCase.getById(databaseHelper.getProductDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<VanPhongPham> getAllProduct() {
        try {
            List<VanPhongPham> list = ProductUseCase.getAll(databaseHelper.getProductDao());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<VanPhongPham> getAllProductByName(String nameProduct) {
        try {
            List<VanPhongPham> list = ProductUseCase.getAllProductByProductName(databaseHelper.getProductDao(), nameProduct);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<VanPhongPham> getTopProducts() {
        try {
            List<VanPhongPham> list = ProductUseCase.getTopProductView(databaseHelper.getProductDao());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // Staft Table ------------------------------------------------------------

    // Thêm nhân viên
    public boolean insert(NhanVien NhanVien) {
        try {
            return  StaftUseCase.create(databaseHelper.getStaftDao(), NhanVien) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    // Cập nhật nhân viên
    public boolean update(NhanVien NhanVien) {
        try {
            StaftUseCase.update(databaseHelper.getStaftDao(), NhanVien);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa nhân viên
    public boolean delete(NhanVien NhanVien) {
        try {
            StaftUseCase.delete(databaseHelper.getStaftDao(), NhanVien);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy nhân viên theo id
    public NhanVien getStaftById(long id) {
        try {
            return StaftUseCase.getById(databaseHelper.getStaftDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    // Lấy toàn bộ danh sách nhân viên
    public List<NhanVien> getAllStaft() {
        try {
            List<NhanVien> list = StaftUseCase.getAll(databaseHelper.getStaftDao());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<NhanVien> getAllStaftByName(String name) {
        try {
            List<NhanVien> list = StaftUseCase.getAllStaftByName(databaseHelper.getStaftDao(), name);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // Allocation Table --------------------------------------------------------

    public boolean insert(CapPhat phieu) {
        try {
            return  AllocationUseCase.create(databaseHelper.getAllocationDao(), phieu) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public boolean update(CapPhat phieu) {
        try {
            AllocationUseCase.update(databaseHelper.getAllocationDao(), phieu);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(CapPhat phieu) {
        try {
            AllocationUseCase.delete(databaseHelper.getAllocationDao(), phieu);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public CapPhat getAllocationById(long id) {

        CapPhat phieu = new CapPhat();
        try {
            phieu = AllocationUseCase.getById(databaseHelper.getAllocationDao(), id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return phieu;

    }

    public List<CapPhat> getAllAllocation() {
        try {
            List<CapPhat> list = AllocationUseCase.getAll(databaseHelper.getAllocationDao());
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CapPhat> getAllAlocationByIdBill(String idBill) {
        try {

            List<CapPhat> list = AllocationUseCase.getAllByIdBill(databaseHelper.getAllocationDao(), idBill);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CapPhat> getAllocationByStaftId(String staftId) {
        try {
            List<CapPhat> list = AllocationUseCase.getByIdStaft(databaseHelper.getAllocationDao(), staftId);
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CapPhat> getAllocationByStaftId(long staftId) {
        try {
            List<CapPhat> list = AllocationUseCase.getByIdStaft(databaseHelper.getAllocationDao(), staftId);
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CapPhat> getAllocationByProductId(String productId) {
        try {
            List<CapPhat> list = AllocationUseCase.getByProductId(databaseHelper.getAllocationDao(), productId);
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<CapPhat> getAllocationByIdProduct(String idProduct) {
        try {
            List<CapPhat> list = AllocationUseCase.getAllByIdProduct(databaseHelper.getAllocationDao(), idProduct);
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }








}
