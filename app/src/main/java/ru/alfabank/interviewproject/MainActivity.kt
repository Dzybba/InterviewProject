package ru.alfabank.interviewproject

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

private const val endpoint = "https://personal.info.com"

class MainActivity : AppCompatActivity() {

    private val service = PersonalInfoServiceFactory().createService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val execute = service.getUsers().execute().body()
        /**
        Что нужно сделать
        1 Все лишние классы вынести из этого файла
        2 Переписать интерфейс сервиса, чтобы поддерживал rx
        3 Результат вызова getUsers нужно отфильтровать, что бы не было null, отсортировать по имени,
        привести список к мапе вида Map<String, PersonalInfo>, где ключем будет name из PersonalInfo
        4 Результат вывести в тост
        5 Сервис PersonalInfoService должен инжектится в активность
        6 Так как сервис не рабочий, нужно сделать временную имплементацию, которая будет возвращать
        фейковые данные
        7 Доработать инжект в эту активность c возможностью выбрать временную имплементацию сервиса
        либо сервис созданный ретрофитом
         **/
    }
}

data class PersonalInfo(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("surname")
    @Expose
    val surname: String,
    @SerializedName("street")
    @Expose
    val street: String,
    @SerializedName("city")
    @Expose
    val city: String,
    @SerializedName("address")
    @Expose
    val address: String
)

interface PersonalInfoService {
    @GET("v1/data")
    fun getUsers(): Call<List<PersonalInfo?>>
}

class PersonalInfoServiceFactory {

    fun createService(): PersonalInfoService {
        val okHttpClient = createOkHttpClient()
        val retrofit = Retrofit.Builder().apply {
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(endpoint)
        }.build()
        return retrofit.create(PersonalInfoService::class.java)
    }
}

private fun createOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder().apply {
        protocols(listOf(Protocol.HTTP_1_1))
    }
    return builder.build()
}