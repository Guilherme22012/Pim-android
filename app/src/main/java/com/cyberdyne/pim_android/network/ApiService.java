package com.cyberdyne.pim_android.network;


import com.cyberdyne.pim_android.models.LoginRequestDto;
import com.cyberdyne.pim_android.models.LoginResponseDto;

import retrofit2.Call; // Objeto do Retrofit que representa uma chamada de API
import retrofit2.http.Body; // Anotação para indicar que o objeto será enviado no corpo da requisição
import retrofit2.http.POST; // Anotação para indicar que é uma requisição POST


public interface ApiService {
    @POST("auth/login")
    Call<LoginResponseDto> login(@Body LoginRequestDto loginRequest); }
