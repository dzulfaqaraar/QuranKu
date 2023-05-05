package com.dzulfaqar.quranku.utils

import com.dzulfaqar.quranku.core.data.Resource
import com.dzulfaqar.quranku.core.domain.model.*
import com.dzulfaqar.quranku.core.utils.Constant
import com.dzulfaqar.quranku.model.*

object AppMapper {

    fun mapSurahDomainToPresentation(state: Resource<List<SurahDomain>>): Resource<List<SurahModel>> {
        return state.mapResource { list ->
            list?.map {
                it.mapToPresentation()
            } ?: arrayListOf()
        }
    }

    fun mapJuzDomainToPresentation(state: Resource<List<JuzDomain>>): Resource<List<JuzModel>> {
        return state.mapResource { list ->
            list?.map {
                it.mapToPresentation()
            } ?: arrayListOf()
        }
    }

    fun mapAyatDomainToPresentation(state: Resource<List<AyatDomain>>): Resource<List<AyatModel>> {
        return state.mapResource { list ->
            mapAyatDomainToPresentation(list ?: arrayListOf())
        }
    }

    fun mapAyatDomainToPresentation(input: List<AyatDomain>): List<AyatModel> {
        return input.map {
            it.mapToPresentation()
        }
    }

    fun mapReciterDomainToPresentation(state: Resource<List<ReciterDomain>>): Resource<List<ReciterModel>> {
        return state.mapResource { list ->
            list?.map {
                mapReciterDomainToPresentation(it)
            } ?: arrayListOf()
        }
    }

    fun mapReciterDomainToPresentation(input: ReciterDomain): ReciterModel = input.mapToPresentation()

    fun mapAudioDomainToPresentation(state: Resource<List<AudioDomain>>): Resource<ArrayList<String>> {
        return state.mapResource { list ->
            val result = arrayListOf<String>()
            result.addAll(list?.map {
                Constant.URL_AUDIO + it.url
            } ?: arrayListOf())
            result
        }
    }

    fun mapSurahModelToDomain(input: SurahModel?): SurahDomain? = input?.mapToDomain()

    fun mapJuzModelToDomain(input: JuzModel?): JuzDomain? = input?.mapToDomain()

    fun mapReciterModelToDomain(input: ReciterModel): ReciterDomain = input.mapToDomain()

    fun mapAyatModelToDomain(input: AyatModel): AyatDomain = input.mapToDomain()
}
