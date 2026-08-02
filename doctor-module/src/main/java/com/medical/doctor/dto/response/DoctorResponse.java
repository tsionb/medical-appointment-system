package com.medical.doctor.dto.response;

import com.medical.doctor.entity.Doctor;


public class DoctorResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private Long departmentId;
    private String departmentName;

    private Double averageRating;

    public DoctorResponse() {}

    public static DoctorResponse fromEntity(Doctor doctor) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setFirstName(doctor.getFirstName());
        response.setLastName(doctor.getLastName());
        response.setEmail(doctor.getEmail());
        response.setPhone(doctor.getPhone());
        response.setSpecialization(doctor.getSpecialization());


        if (doctor.getDepartment() != null) {
            response.setDepartmentId(doctor.getDepartment().getId());
            response.setDepartmentName(doctor.getDepartment().getName());
        }

        return response;
    }

  
    public static DoctorResponse fromEntity(Doctor doctor, Double averageRating) {
        DoctorResponse response = fromEntity(doctor);
        response.setAverageRating(averageRating);
        return response;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }
}