<template>
  <main>
    <div class="box" style="text-align: center">
      <h1>Shor URL Generater</h1>
      <label for="long" class="inp">
        <input type="text" id="long" v-model="input" />
        <span class="label">长链接</span>
        <span class="focus-bg"></span>
      </label>
      <button type="button" @click="generate">生成短链接</button>
      <div>
        <label for="short" class="inp short">
          <input type="text" id="short" v-model="shortURL" />
          <span class="label">短链接</span>
          <span class="focus-bg"></span>
        </label>
        <button type="button" style="width: 100px" @click="copy">复制</button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const input = ref('')
const shortURL = ref('')

const generate = () => {
  const send = { longURL: input.value }
  console.log(send)
  axios.post('/api/generate', send).then((res) => {
    console.log(res.data)
    shortURL.value = res.data.data
  })
}

const copy = () => {
  // copy shortURL to clipboard
  navigator.clipboard.writeText(shortURL.value)
}
</script>

<style scoped></style>
