<script setup lang="ts"></script>

<template>
	<div class="custombox">
        <slot />
    </div>
</template>

<style scoped>
	.custombox {
        border: 3px solid rgb(90, 90, 90);
        border-radius: 10px;
        padding: 20px;
        box-shadow: 0px 0px 12px 4px #888888;
    }
</style>

